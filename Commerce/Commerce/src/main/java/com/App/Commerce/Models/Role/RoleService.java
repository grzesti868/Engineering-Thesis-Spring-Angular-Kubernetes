/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Role;

public interface RoleService {
    Role saveRole(final Role role);
    void addRoleToUser(final String username, String roleName);
}
