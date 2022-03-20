/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Configs;

import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Enums.StatusEnum;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.AppUser.AppUserRepository;
import com.App.Commerce.Models.AppUser.AppUserService;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Role.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class basicConfiguration {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    CommandLineRunner commandLineRunner(AppUserService appUserService, AppUserRepository appUserRepository) {
        return args -> {
            AppUserEntity user1 = new AppUserEntity("greg","pass","email",  StatusEnum.Active);
            AppUserEntity user2 = new AppUserEntity("greg2","pass2","email2",  StatusEnum.Active);

            PersonEntity person = new PersonEntity("Grzegorz","Stich", LocalDate.now(), SexEnum.Male);
            PersonEntity person2 = new PersonEntity("Grz22egorz","S22tich", LocalDate.now(), SexEnum.Male);
            AddressEntity address = new AddressEntity("Street1","12a","2","Warszawa","12-345","Polska");
            AddressEntity address2 = new AddressEntity("Street21","12a2","22","Wa2rszawa","12-345","Polska");
            person.setAddressEntity(address);
            person2.setAddressEntity(address2);
            user1.setPersonEntity(person);
            user2.setPersonEntity(person2);
            //todo: basic Role do jakiegos enum??
            appUserService.saveRole(new Role("ROLE_USER"));
            appUserService.saveRole(new Role("ROLE_MANAGER"));
            appUserService.saveRole(new Role("ROLE_ADMIN"));
            appUserService.saveRole(new Role("ROLE_SUPER_ADMIN"));

            appUserService.addUser(user1);
            appUserService.addUser(user2);
            //appUserRepository.save(user2);

            appUserService.addRoleToUser(user2.getUsername(),"ROLE_USER");
            appUserService.addRoleToUser(user2.getUsername(),"ROLE_MANAGER");
            appUserService.addRoleToUser(user1.getUsername(),"ROLE_ADMIN");
            appUserService.addRoleToUser(user1.getUsername(),"ROLE_USER");



        };

    }
}
