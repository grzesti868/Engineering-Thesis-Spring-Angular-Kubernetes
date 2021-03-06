package com.App.Commerce.Models.Person;


import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.Order.OrderEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "persons")
public class PersonEntity {


    @Id
    @SequenceGenerator(name = "person_sequence", sequenceName = "person_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_sequence")
    private Long id;

    @Column(nullable = false, name = "firstname")
    private String firstname;

    @Column(nullable = false, name = "lastname")
    private String lastname;

    @Column(nullable = false, name = "birthDate")
    private LocalDate birthDate;

    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date createdAt;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @Column(nullable = false, name = "sex")
    private SexEnum sex;

    @OneToOne(targetEntity = AddressEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_address", referencedColumnName = "id")
    private AddressEntity addressEntity;

    @OneToOne(mappedBy = "personEntity")
    private AppUserEntity appUserEntity;


    public PersonEntity(String firstname, String lastname, LocalDate birthDate, SexEnum sex) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.sex = sex;
    }
}
