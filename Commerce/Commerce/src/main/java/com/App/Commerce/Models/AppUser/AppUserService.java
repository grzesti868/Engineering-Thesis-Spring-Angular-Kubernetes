package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Models.Role.Role;
import com.App.Commerce.Models.Role.RoleService;

import java.util.List;

public interface AppUserService extends RoleService {
    List<AppUserEntity> getAll();
    Long addUser(final AppUserEntity user);
    AppUserEntity getUser(final String username);
    void validateUserDetails(final AppUserEntity user);
    AppUserEntity update(final String username, final AppUserEntity updateUser);
    void deleteByUsername(final String username);

}
