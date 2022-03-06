package com.App.Commerce.Models.Person;

import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.Role.Role;

import java.util.List;

public interface PersonService {

    List<AppUserEntity> getAll();
    Long add(final AppUserEntity user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    //todo: wyswetil pierwsze 10? userow zamiast cala tabele?
    AppUserEntity getUser(String username);
    boolean validatePersonDetails(PersonEntity personEntity);
}
