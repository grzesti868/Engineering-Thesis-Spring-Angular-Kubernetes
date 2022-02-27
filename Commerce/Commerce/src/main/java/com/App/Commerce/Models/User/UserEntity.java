package com.App.Commerce.Models.User;


import com.App.Commerce.Enums.StatusEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private Date updatedOn;

    @Column(nullable = false, name = "status")
    private StatusEnum status;

    public UserEntity(String username, String password, String email, StatusEnum status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }

}
