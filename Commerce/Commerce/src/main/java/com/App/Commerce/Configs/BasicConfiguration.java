/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Configs;

import com.App.Commerce.Enums.OrderStatusEnum;
import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Enums.UserStatusEnum;
import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.AppUser.AppUserService;
import com.App.Commerce.Models.Order.OrderEntity;
import com.App.Commerce.Models.Order.OrderService;
import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.App.Commerce.Models.OrderDetails.OrderDetailsService;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import com.App.Commerce.Models.Product.ProductService;
import com.App.Commerce.Models.Role.Role;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.LocalDate;

@Configuration
@Slf4j
@Transactional
public class BasicConfiguration {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            AppUserService appUserService,
            ProductService productService,
            OrderService orderService,
            OrderDetailsService orderDetailsService
            ) {
        return args -> {
            try {
                log.debug("...USER CREATION...");
                AppUserEntity user1 = new AppUserEntity("greg1", "pass1", "email", UserStatusEnum.Active);
                AppUserEntity user2 = new AppUserEntity("greg2", "pass2", "email2", UserStatusEnum.Active);
                AppUserEntity user3 = new AppUserEntity("greg3", "pass3", "email23", UserStatusEnum.Active);
                log.debug("...USER COMPLETED...");
                log.debug("...PERSONS CREATION...");
                PersonEntity person = new PersonEntity("Grzegorz", "Stich", LocalDate.now(), SexEnum.Male);
                PersonEntity person2 = new PersonEntity("Andrzej", "Nazwisko2", LocalDate.now(), SexEnum.Male);
                PersonEntity person3 = new PersonEntity("Maksymilian", "Nazwisko3", LocalDate.now(), SexEnum.Male);
                log.debug("...USER COMPLETED...");

                log.debug("...ADDRESSES COMPLETED...");
                AddressEntity address = new AddressEntity("Street1", "12a", "2", "Warszawa", "12-345", "Polska");
                AddressEntity address2 = new AddressEntity("Street21", "12a2", "22", "Wa2rszawa", "12-345", "Polska");
                AddressEntity address3 = new AddressEntity("Strasdeet21", "12a2", "22", "Wa2rszawa", "12-345", "Polska");
                log.debug("...ADDRESSES COMPLETED...");
                person.setAddressEntity(address);
                person2.setAddressEntity(address2);
                person3.setAddressEntity(address3);
                user1.setPersonEntity(person);
                user2.setPersonEntity(person2);
                user3.setPersonEntity(person3);
                log.debug("...ROLE CREATION...");
                appUserService.saveRole(new Role("ROLE_CUSTOMER"));
                appUserService.saveRole(new Role("ROLE_MANAGER"));
                appUserService.saveRole(new Role("ROLE_ADMIN"));

                appUserService.addUser(user1);
                appUserService.addUser(user2);
                appUserService.addUser(user3);

                appUserService.addRoleToUser(user3.getUsername(), "ROLE_CUSTOMER");
                appUserService.addRoleToUser(user2.getUsername(), "ROLE_MANAGER");
                appUserService.addRoleToUser(user1.getUsername(), "ROLE_ADMIN");

                CurrencyUnit pln = Monetary.getCurrency("PLN");
                Money moneyOf = Money.of(12, pln);
                Money moneyOf1 = Money.of(13, pln);
                Money moneyOf2 = Money.of(3, pln);

                ProductEntity product1 = new ProductEntity("produkt1", moneyOf, 10, "imgFile");
                ProductEntity product2 = new ProductEntity("produkt2", moneyOf1, 102, "imgFile1");
                ProductEntity product3 = new ProductEntity("produkt3", moneyOf2, 103, "imgFile2");

                productService.addProduct(product1);
                productService.addProduct(product2);
                productService.addProduct(product3);

                log.debug("...ORDER CREATION...");
                AddressEntity orderAddress = new AddressEntity("orderAddressStreeet", "12a", "2", "Warszawa", "12-345", "Polska");
                OrderEntity orderEntity = new OrderEntity(OrderStatusEnum.New, user3, orderAddress);
                Long OrderId = orderService.addOrder(orderEntity);
                OrderDetailsEntity orderDetails = new OrderDetailsEntity(1, product1);
                OrderDetailsEntity orderDetails2 = new OrderDetailsEntity(1, product2);
                orderService.addOrderDetailToOrder(orderDetails, OrderId);
                orderService.addOrderDetailToOrder(orderDetails2, OrderId);

                log.debug("...ORDER CREATION COMPLETED...");
            }catch(ApiRequestException | ApiNotFoundException e ) {
                log.info("DATA CREATION FAILED, PERHAPS DATA ALREADY IN DATABASE!");
            }
        };

    }
}
