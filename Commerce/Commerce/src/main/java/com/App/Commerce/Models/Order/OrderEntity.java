/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "order")
public class OrderEntity {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;

    @Column(nullable = false, name = "product_quantity")
    private Integer productQuantity;

    @ManyToOne(targetEntity = PersonEntity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FkPerson")
    private PersonEntity buyer;

    @OneToMany(mappedBy="order")
    private Set<OrderDetailsEntity> orderDetails;
}
