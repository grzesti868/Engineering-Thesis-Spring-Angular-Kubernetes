package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Person.PersonService;
import com.App.Commerce.Models.Role.Role;
import com.App.Commerce.Models.Role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PersonService personService;

    @Override
    public List<AppUserEntity> getAll() {
        return appUserRepository.findAll();
    }


    @Override
    public Long add(AppUserEntity user) {
        AppUserEntity newUser = Optional.ofNullable(user)
                .orElseThrow(() -> new ApiRequestException("New user can not be empty."));

        validateUserDetails(newUser);

        personService.validatePersonDetails(newUser.getPersonEntity());


        return appUserRepository.save(newUser).getId();
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUserEntity user = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.", username)));

        Role role = Optional.ofNullable(roleRepository.findByName(roleName))
                .orElseThrow(() -> new ApiNotFoundException(String.format("Role %s was not found.", roleName)));


        user.getRoles().add(role);
    }

    @Override
    public AppUserEntity getUser(String username) {
        return Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException(String.format("User %s was not found.", username)));

    }


    @Override
    public void validateUserDetails(AppUserEntity user) {

        if (appUserRepository.existsByUsername(user.getUsername()))
            throw new ApiRequestException("Username already exists.");

        if (appUserRepository.existsByEmail(user.getEmail()))
            throw new ApiRequestException("User's email already taken");

    }

    @Override
    public AppUserEntity update(String username, AppUserEntity user) {

        AppUserEntity updateUser = Optional.ofNullable(user)
                .orElseThrow(() -> new ApiRequestException("User can not be empty."));

        AppUserEntity userToUpdate = Optional.ofNullable(appUserRepository.findByUsername(username))
                .orElseThrow(() -> new ApiNotFoundException("User to update does not exists"));

        if (appUserRepository.existsByUsername(username))
            userToUpdate.setUsername(updateUser.getUsername());
        else
            throw new ApiRequestException("Username already taken");


        if (appUserRepository.existsByEmail(updateUser.getEmail()))
            userToUpdate.setEmail(updateUser.getUsername());
        else
            throw new ApiRequestException("Email already taken");

        userToUpdate.setPassword(updateUser.getPassword());
        userToUpdate.setStatus(updateUser.getStatus());

        PersonEntity updatePerson = Optional.ofNullable(updateUser.getPersonEntity())
                .orElseThrow(() -> new ApiNotFoundException("Person details can not be empty."));

        //todo: impl
        //personService.update(updatePerson)


        return appUserRepository.save(userToUpdate);

    }
}

