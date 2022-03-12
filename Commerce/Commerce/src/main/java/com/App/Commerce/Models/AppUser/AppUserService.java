package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Models.Role.Role;

import java.util.List;

public interface AppUserService {
    //todo: wyswetil pierwsze 10? userow zamiast cala tabele?
    List<AppUserEntity> getAll();
    Long addUser(final AppUserEntity user);
    Role saveRole(final Role role);
    void addRoleToUser(final String username, String roleName);
    AppUserEntity getUser(final String username);
    void validateUserDetails(final AppUserEntity user);
    AppUserEntity update(final String username, final AppUserEntity user);
    void deleteByUsername(final String username);

}
