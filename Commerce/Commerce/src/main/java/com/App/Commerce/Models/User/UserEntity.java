package com.App.Commerce.Models.User;


import com.App.Commerce.Enums.StatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor

public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDate birthDate;
    private StatusEnum status;

    public UserEntity(String username, String password, String email, LocalDate birthDate, StatusEnum status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.status = status;
    }
}
