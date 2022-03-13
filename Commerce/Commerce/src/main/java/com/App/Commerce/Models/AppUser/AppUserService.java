package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Models.Role.Role;

import java.util.List;

public interface AppUserService {
    List<AppUserEntity> getAll();
    Long addUser(final AppUserEntity user);
    //todo: refaktor role to wlasnego package
    Role saveRole(final Role role);
    void addRoleToUser(final String username, String roleName);
    AppUserEntity getUser(final String username);
    void validateUserDetails(final AppUserEntity user);
    AppUserEntity update(final String username, final AppUserEntity updateUser);
    void deleteByUsername(final String username);

}
