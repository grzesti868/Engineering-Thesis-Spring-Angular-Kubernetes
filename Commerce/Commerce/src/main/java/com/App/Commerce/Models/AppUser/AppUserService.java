package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Models.Role.Role;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    List<AppUserEntity> getAll();
    Long add(final AppUserEntity user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    //todo: wyswetil pierwsze 10? userow zamiast cala tabele?
    AppUserEntity getUser(String username);
    void validateUserDetails(AppUserEntity user);
    AppUserEntity update(String username, final AppUserEntity user);

}
