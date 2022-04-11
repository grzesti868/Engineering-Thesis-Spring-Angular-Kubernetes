/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Person.Role;

import com.App.Commerce.Exceptions.ApiRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;


    @Override
    public Role saveRole(Role role) {
        if(role.getName().startsWith("ROLE_") && !roleRepository.existsByName(role.getName())) {
            log.info("Add role {} to database.", role.getName());
            return roleRepository.save(role);
        } else {
            throw new ApiRequestException("Role: "+role.getName()+" can not be added.");
        }
    }

}
