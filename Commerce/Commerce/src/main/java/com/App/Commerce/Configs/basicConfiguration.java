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
import com.App.Commerce.Models.Order.OrderEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import com.App.Commerce.Models.Product.ProductService;
import com.App.Commerce.Models.Role.Role;
import org.javamoney.moneta.Money;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class basicConfiguration {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    CommandLineRunner commandLineRunner(AppUserService appUserService, ProductService productService) {
        return args -> {
            AppUserEntity user1 = new AppUserEntity("greg1","pass1","email",  StatusEnum.Active);
            AppUserEntity user2 = new AppUserEntity("greg2","pass2","email2",  StatusEnum.Active);
            AppUserEntity user3 = new AppUserEntity("greg3","pass3","email23",  StatusEnum.Active);

            PersonEntity person = new PersonEntity("Grzegorz","Stich", LocalDate.now(), SexEnum.Male);
            PersonEntity person2 = new PersonEntity("Grz22egorz","S22tich", LocalDate.now(), SexEnum.Male);
            PersonEntity person3 = new PersonEntity("Grz22asdegorz","S22tich", LocalDate.now(), SexEnum.Male);


            AddressEntity address = new AddressEntity("Street1","12a","2","Warszawa","12-345","Polska");
            AddressEntity address2 = new AddressEntity("Street21","12a2","22","Wa2rszawa","12-345","Polska");
            AddressEntity address3 = new AddressEntity("Strasdeet21","12a2","22","Wa2rszawa","12-345","Polska");
            person.setAddressEntity(address);
            person2.setAddressEntity(address2);
            person3.setAddressEntity(address3);
            user1.setPersonEntity(person);
            user2.setPersonEntity(person2);
            user3.setPersonEntity(person3);
            appUserService.saveRole(new Role("ROLE_CUSTOMER"));
            appUserService.saveRole(new Role("ROLE_MANAGER"));
            appUserService.saveRole(new Role("ROLE_ADMIN"));

            appUserService.addUser(user1);
            appUserService.addUser(user2);
            appUserService.addUser(user3);

            appUserService.addRoleToUser(user3.getUsername(),"ROLE_CUSTOMER");
            appUserService.addRoleToUser(user2.getUsername(),"ROLE_MANAGER");
            appUserService.addRoleToUser(user1.getUsername(),"ROLE_ADMIN");

            CurrencyUnit usd = Monetary.getCurrency("PLN");
            Money moneyof = Money.of(12, usd);

            ProductEntity product1 = new ProductEntity("produkt1",moneyof,10, "imgFile");
            ProductEntity product2 = new ProductEntity("produkt2",moneyof,102, "imgFile1");
            ProductEntity product3 = new ProductEntity("produkt3",moneyof,103, "imgFile2");

            productService.addProduct(product1);
            productService.addProduct(product2);
            productService.addProduct(product3);



        };

    }
}
