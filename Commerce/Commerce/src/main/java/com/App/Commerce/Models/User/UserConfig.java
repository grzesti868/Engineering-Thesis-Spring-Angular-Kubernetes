package com.App.Commerce.Models.User;

import com.App.Commerce.Enums.StatusEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            UserEntity user1 = new UserEntity("greg","pass","email",  StatusEnum.Active);
            UserEntity user2 = new UserEntity("greg2","pass2","email2",  StatusEnum.Active);

            userRepository.saveAll(List.of(user1,user2));

        };

    }
}
