package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Enums.StatusEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppUserConfig {

    @Bean
    CommandLineRunner commandLineRunner(AppUserRepository appUserRepository) {
        return args -> {
            AppUserEntity user1 = new AppUserEntity("greg","pass","email",  StatusEnum.Active);
            AppUserEntity user2 = new AppUserEntity("greg2","pass2","email2",  StatusEnum.Active);

            appUserRepository.saveAll(List.of(user1,user2));

        };

    }
}
