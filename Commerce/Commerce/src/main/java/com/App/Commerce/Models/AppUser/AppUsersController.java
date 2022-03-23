package com.App.Commerce.Models.AppUser;


import com.App.Commerce.Configs.JwtConfigProperties;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Filter.CustomAlgorithmImpl;
import com.App.Commerce.Models.Role.Role;
import com.App.Commerce.Models.Role.RoleService;
import com.App.Commerce.Models.RoleToUserForm.RoleToUserForm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppUsersController {

    private final AppUserService appUserService;
    private final JwtConfigProperties jwtConfigProperties;
    private final CustomAlgorithmImpl customAlgorithm;

    @GetMapping("/users")
    public ResponseEntity<List<AppUserEntity>> listAllUsers() {
        final List<AppUserEntity> userList = appUserService.getAll();

        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("{username}")
    public ResponseEntity<AppUserEntity> getUserByName(@PathVariable final String username) {
        return ResponseEntity.ok().body(appUserService.getUser(username));
    }

    //todo: czy potrzebne consumes = APPLICATION_JSON_VALUE
    @PostMapping("/user/save")
    public ResponseEntity<String> addUser(@RequestBody final AppUserEntity user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/u/save").toUriString());
        return ResponseEntity.created(uri).body("User has been added, Id: " + appUserService.addUser(user));
    }
    //todo: usunalem path = -> ok?
    @PutMapping("{username}")
    public ResponseEntity<AppUserEntity> updateUserByUsername(
            @PathVariable final String username,
            @RequestBody final AppUserEntity user) {

        return ResponseEntity.ok(appUserService.update(username, user));
    }

    @DeleteMapping("{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") final String username){
        appUserService.deleteByUsername(username);
        return ResponseEntity.ok("User has been deleted.");
    }
    //todo: refaktor to role service?
    @PostMapping("/role/save")
    public ResponseEntity<String> addRole(@RequestBody final Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/role/save").toUriString());
        return ResponseEntity.created(uri).body("Role has been added, name: " + appUserService.saveRole(role).getName());
    }
    @PostMapping("/role/addtouser")
    public ResponseEntity<String> addRoleToUser(@RequestBody final RoleToUserForm form) {
        appUserService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }
    //todo: refaktor to token service?
    @GetMapping("/token/refresh")
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
                AppUserEntity user = appUserService.getUser(username);
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


    //todo: refaktor?


/*
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserRestModel>> listAllUsers() {
        final List<UserRestModel> userList = userService.getAll();

        return ResponseEntity.ok(userList);
    }

    @GetMapping("{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    public ResponseEntity<UserRestModel> getUserByName(@PathVariable final String username) {

        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@RequestBody final UserRestModel user) {
        return ResponseEntity.ok("User has been added, Id: " + userService.add(user));
    }

    @PutMapping("{username}")
    @PreAuthorize("hasAuthority('address:write') or #username == authentication.name")
    public ResponseEntity<UserRestModel> updateUserByUsername(
            @PathVariable final String username,
            @RequestBody final UserRestModel user) {
        return ResponseEntity.ok(userService.update(username,user));
    }

    @DeleteMapping(path = "{username}")
    @PreAuthorize("hasAuthority('address:write') or #username == authentication.name")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") final String username){
        userService.deleteByUsername(username);
        return ResponseEntity.ok("User has been deleted.");
    }

 */
}
