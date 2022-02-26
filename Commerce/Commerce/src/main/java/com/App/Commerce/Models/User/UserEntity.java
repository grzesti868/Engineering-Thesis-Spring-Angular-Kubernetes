package com.App.Commerce.Models.User;


import com.App.Commerce.Enums.StatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
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
