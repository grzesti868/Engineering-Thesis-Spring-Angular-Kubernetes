package com.App.Commerce.Models.Address;


import com.App.Commerce.Enums.SexEnum;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "addresses")
public class AddressEntity {

    @Id
    @SequenceGenerator(name = "address_sequence", sequenceName = "address_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequence")
    private Long id;

    @Column(nullable = false, name = "street")
    private String street;

    @Column(nullable = false, name = "building_num")
    private String building_num;

    @Column(name = "apartment_num")
    private String apartment_num;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "postal_code")
    private String postal_code;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date createdAt;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @OneToOne(mappedBy = "addressEntity", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private PersonEntity personEntity;

    public AddressEntity(String street, String building_num, String apartment_num, String city, String postal_code, String country) {
        this.street = street;
        this.building_num = building_num;
        this.apartment_num = apartment_num;
        this.city = city;
        this.postal_code = postal_code;
        this.country = country;
    }
}
