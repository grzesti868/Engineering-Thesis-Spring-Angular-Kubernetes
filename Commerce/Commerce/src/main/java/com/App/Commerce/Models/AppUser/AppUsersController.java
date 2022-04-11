package com.App.Commerce.Models.AppUser;


import com.App.Commerce.Models.Person.Role.Role;
import com.App.Commerce.Models.RoleToUserForm.RoleToUserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AppUsersController {

    private final AppUserService appUserService;

    @GetMapping("/all")
   // @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AppUserEntity>> listAllUsers() {
        final List<AppUserEntity> userList = appUserService.getAll();

        return ResponseEntity.ok().body(userList);
    }

    @GetMapping("{username}")
    //todo: przetestowac
   // @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    public ResponseEntity<AppUserEntity> getUserByName(@PathVariable final String username) {
        return ResponseEntity.ok().body(appUserService.getUser(username));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody final AppUserEntity user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/add").toUriString());
        return ResponseEntity.created(uri).body("User has been added, Id: " + appUserService.addUser(user));
    }

    @PutMapping("{username}")
   // @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    public ResponseEntity<AppUserEntity> updateUserByUsername(
            @PathVariable final String username,
            @RequestBody final AppUserEntity user) {

        return ResponseEntity.ok(appUserService.update(username, user));
    }

    @DeleteMapping("{username}")
  //  @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.name")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable("username") final String username){
        appUserService.deleteByUsername(username);
        return ResponseEntity.ok("User has been deleted.");
    }

    @PostMapping("/role/add")
 //   @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addRole(@RequestBody final Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/role/save").toUriString());
        return ResponseEntity.created(uri).body("Role has been added, name: " + appUserService.saveRole(role).getName());
    }
    @PostMapping("/role/addtouser")
   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addRoleToUser(@RequestBody final RoleToUserForm form) {
        appUserService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().body("Role " + form.getRolename() +" has been added to user: "+ form.getUsername());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        appUserService.refreshToken(request,response);
    }

/*

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
