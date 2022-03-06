package com.App.Commerce.Models.Employee;


import com.App.Commerce.Models.AppUser.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @SequenceGenerator(name = "employee_sequence", sequenceName = "employee_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence")
    private Long id;


    @OneToOne(targetEntity = AppUserEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "FkUser", referencedColumnName = "id")
    private AppUserEntity appUserEntity;
}
