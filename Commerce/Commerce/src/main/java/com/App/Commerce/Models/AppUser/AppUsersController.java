package com.App.Commerce.Models.AppUser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class AppUsersController {

    private final AppUserService appUserService;

    @Autowired
    public AppUsersController(final AppUserService appUserService) {
        this.appUserService = appUserService;
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
