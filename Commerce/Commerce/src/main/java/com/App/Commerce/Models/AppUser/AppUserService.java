package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Models.Person.Role.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AppUserService extends RoleService {
    List<AppUserEntity> getAll();
    Long addUser(final AppUserEntity user);
    AppUserEntity getUser(final String username);
    void validateUserDetails(final AppUserEntity user);
    AppUserEntity update(final String username, final AppUserEntity updateUser);
    void deleteByUsername(final String username);
    void addRoleToUser(String username, String roleName);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    }
