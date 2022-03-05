package com.App.Commerce.Models.Customer;


import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.User.UserEntity;
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

    @OneToOne(targetEntity = UserEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FkUser", referencedColumnName = "id")
    private UserEntity userEntity;
}
