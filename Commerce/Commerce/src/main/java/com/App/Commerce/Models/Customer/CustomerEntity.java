package com.App.Commerce.Models.Customer;


import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.AppUser.AppUserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @SequenceGenerator(name = "customer_sequence", sequenceName = "customer_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_sequence")
    private Long id;

    @OneToOne(targetEntity = AppUserEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FkUser", referencedColumnName = "id")
    private AppUserEntity appUserEntity;
}
