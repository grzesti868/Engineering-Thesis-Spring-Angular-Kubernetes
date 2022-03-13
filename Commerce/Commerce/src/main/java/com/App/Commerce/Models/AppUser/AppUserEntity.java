package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Enums.StatusEnum;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Role.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class AppUserEntity {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @Setter(value = AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @Column(nullable = false, name = "status")
    private StatusEnum status;

    @OneToOne(targetEntity = PersonEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FkPerson", referencedColumnName = "id")
    private PersonEntity personEntity;



    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JoinColumn(name = "FkPerson", referencedColumnName = "id")
    private Collection<Role> roles= new ArrayList<>();

    public AppUserEntity(String username, String password, String email, StatusEnum status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
    }

}