package com.App.Commerce.Models.AppUser;


import com.App.Commerce.Models.Role.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUsersController {

    private final AppUserService appUserService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserEntity>> listAllUsers() {
        final List<AppUserEntity> userList = appUserService.getAll();

        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("{username}")
    public ResponseEntity<AppUserEntity> getUserByName(@PathVariable final String username) {

        return ResponseEntity.ok().body(appUserService.getUser(username));
    }

    //todo: czy potrzebne consumes = APPLICATION_JSON_VALUE
    @PostMapping(path = "/u/save")
    public ResponseEntity<String> addUser(@RequestBody final AppUserEntity user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/u/save").toUriString());
        return ResponseEntity.created(uri).body("User has been added, Id: " + appUserService.addUser(user));
    }

    @PutMapping("{username}")
    public ResponseEntity<AppUserEntity> updateUserByUsername(
            @PathVariable final String username,
            @RequestBody final AppUserEntity user) {

        return ResponseEntity.ok(appUserService.update(username, user));
    }

    @DeleteMapping(path = "{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") final String username){
        appUserService.deleteByUsername(username);
        return ResponseEntity.ok("User has been deleted.");
    }

    @PostMapping(path = "/role/save")
    public ResponseEntity<String> addRole(@RequestBody final Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/role/save").toUriString());
        return ResponseEntity.created(uri).body("Role has been added, name: " + appUserService.saveRole(role).getName());
    }

    @PostMapping(path = "/role/bound")
    public ResponseEntity<String> addRoleToUser(@RequestBody final RoleToUserForm form) {
        appUserService.addRoleToUser(form.getUsername(), form.rolename);
        return ResponseEntity.ok().build();
    }

    //todo: refaktor?
    @Data
    class RoleToUserForm {
        private String username;
        private String rolename;
    }

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
