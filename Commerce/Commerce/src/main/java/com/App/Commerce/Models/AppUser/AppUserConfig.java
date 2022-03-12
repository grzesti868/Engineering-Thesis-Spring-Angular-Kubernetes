package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Enums.StatusEnum;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class AppUserConfig {

    @Bean
    CommandLineRunner commandLineRunner(AppUserRepository appUserRepository) {
        return args -> {
           AppUserEntity user1 = new AppUserEntity("greg","pass","email",  StatusEnum.Active);
           AppUserEntity user2 = new AppUserEntity("greg2","pass2","email2",  StatusEnum.Active);

           PersonEntity person = new PersonEntity("Grzegorz","Stich", LocalDate.now(), SexEnum.Male);
           AddressEntity address = new AddressEntity("Street1","12a","2","Warszawa","12-345","Polska");
           person.setAddressEntity(address);
           user1.setPersonEntity(person);



            appUserRepository.saveAll(List.of(user1,user2));

        };

    }
}
