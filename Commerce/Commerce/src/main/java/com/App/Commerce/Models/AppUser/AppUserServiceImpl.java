package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Configs.JwtConfigProperties;
import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Filter.CustomAlgorithmImpl;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Person.PersonService;
import com.App.Commerce.Models.Person.Role.Role;
import com.App.Commerce.Models.Person.Role.RoleRepository;
import com.App.Commerce.Models.Person.Role.RoleService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService{
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtConfigProperties jwtConfigProperties;
    private final CustomAlgorithmImpl customAlgorithm;

    @Override
    public List<AppUserEntity> getAll() {
        log.info("Fetching all users.");
        return appUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //todo: error not found zwracac w body requesta
        AppUserEntity user = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.",username)));
        log.info("User found in the database: {}", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new User(user.getUsername(), user.getPassword(), authorities );

    }


    @Override
    public Role saveRole(Role role) {
        return roleService.saveRole(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to a user {}", roleName, username );

        AppUserEntity user = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.", username)));

        Role role = Optional.ofNullable(roleRepository.findByName(roleName))
                .orElseThrow(() -> new ApiNotFoundException(String.format("Role %s was not found.", roleName)));


        user.getRoles().add(role);
        log.info("Role was added.");

    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String prefix = jwtConfigProperties.getPrefix();
        String claim = jwtConfigProperties.getClaimName();
        Integer accessTokenExpiration= jwtConfigProperties.getAccessTokenExpiration();

        if(authorizationHeader !=null && authorizationHeader.startsWith(prefix)) {
            try{
                String refreshToken = authorizationHeader.substring(prefix.length());
                Algorithm algorithm = customAlgorithm.getAlgorith();
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUserEntity user = getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new java.sql.Date(System.currentTimeMillis() + accessTokenExpiration ))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(claim, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception err) {
                response.setHeader("error", err.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", err.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new ApiRequestException("Refresh token is missing!");
        }

    }

    @Override
    public Long addUser(AppUserEntity user) {
        log.info("Saving new user {} to database", user.getUsername() );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User's password encoded: {}.", user.getPassword());

        if (appUserRepository.existsByUsername(user.getUsername()))
            throw new ApiRequestException("Username already exists.");

        if (appUserRepository.existsByEmail(user.getEmail()))
            throw new ApiRequestException("User's email already taken");

        validateUserDetails(user);

        personService.validatePersonDetails(user.getPersonEntity());

        return appUserRepository.save(user).getId();
    }


    @Override
    public AppUserEntity getUser(String username) {
        log.info("Fetching user {} form database", username);
        return Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.", username)));

    }


    @Override
    public void validateUserDetails(AppUserEntity user) {
        log.info("Validating user {}...", user.getUsername());

        Optional.ofNullable(user)
                .orElseThrow(() -> new ApiRequestException("New user can not be empty."));

        Optional.ofNullable(user.getUsername())
                .orElseThrow(() -> new ApiRequestException("Username can not be empty."));


        Optional.ofNullable(user.getPassword())
                .orElseThrow(() -> new ApiRequestException("Password can not be empty."));

        Optional.ofNullable(user.getEmail())
                .orElseThrow(() -> new ApiRequestException("Email can not be empty."));

        Optional.ofNullable(user.getStatus())
                .orElseThrow(() -> new ApiRequestException("Status can not be empty."));

        log.info("Validation of user {} complete!", user.getUsername());
    }

    @Override
    public AppUserEntity update(String username, AppUserEntity updateUser) {
        log.info("Updating user {}...", username);
        AppUserEntity userToUpdate = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException("User to update does not exists"));


        validateUserDetails(updateUser);

        userToUpdate.setEmail(updateUser.getEmail());
        userToUpdate.setUsername(updateUser.getUsername());
        userToUpdate.setPassword(updateUser.getPassword());
        userToUpdate.setStatus(updateUser.getStatus());
        userToUpdate.setOrders(updateUser.getOrders());

        PersonEntity updatePerson = Optional.ofNullable(updateUser.getPersonEntity())
                .orElseThrow(() -> new ApiNotFoundException("Person's details can not be empty."));

        personService.validatePersonDetails(updatePerson);
        userToUpdate.setPersonEntity(updatePerson);


        return appUserRepository.save(userToUpdate);

    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Deleting user {} from database.", username);
        if (appUserRepository.existsByUsername(username))
            appUserRepository.deleteByUsername(username);
        else
            throw new ApiNotFoundException(String.format("User by username: %s does not exists", username));
    }


}

