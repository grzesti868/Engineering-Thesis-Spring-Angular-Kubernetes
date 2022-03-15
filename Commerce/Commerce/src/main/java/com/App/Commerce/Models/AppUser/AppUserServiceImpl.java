package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Person.PersonService;
import com.App.Commerce.Models.Role.Role;
import com.App.Commerce.Models.Role.RoleRepository;
import com.App.Commerce.Models.Role.RoleService;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService{
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AppUserEntity> getAll() {
        log.info("Fetching all users.");
        return appUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
        log.info("Saving new role {} to database", role.getName());
        return roleRepository.save(role);

    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to a user {}", roleName, username );

        AppUserEntity user = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.", username)));

        Role role = Optional.ofNullable(roleRepository.findByName(roleName))
                .orElseThrow(() -> new ApiNotFoundException(String.format("Role %s was not found.", roleName)));


        user.getRoles().add(role);
        //TODO: jesli zadziala usunac transactional;
        appUserRepository.save(user);
    }

    @Override
    public Long addUser(AppUserEntity user) {
        log.info("Saving new user {} to database", user.getUsername() );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User's password encoded: {}.", user.getPassword());
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

        if (appUserRepository.existsByUsername(user.getUsername()))
            throw new ApiRequestException("Username already exists.");

        if (appUserRepository.existsByEmail(user.getEmail()))
            throw new ApiRequestException("User's email already taken");


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

