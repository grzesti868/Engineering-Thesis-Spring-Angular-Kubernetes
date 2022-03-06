package com.App.Commerce.Models.Person;


import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.Role.Role;
import lombok.AccessLevel;
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

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @Column(nullable = false, name = "sex")
    private SexEnum sex;

    @OneToOne(targetEntity = AddressEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FkAddress", referencedColumnName = "id")
    private AddressEntity addressEntity;


    public PersonEntity(String firstname, String lastname, LocalDate birthDate, SexEnum sex) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.sex = sex;
    }
}
