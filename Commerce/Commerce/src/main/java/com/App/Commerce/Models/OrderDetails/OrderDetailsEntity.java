/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Models.Order.OrderEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
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
@Table(name = "order_details")
public class OrderDetailsEntity {

    @Id
    @SequenceGenerator(name = "order_details_sequence", sequenceName = "order_details_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_details_sequence")
    private Long id;

    @Column(nullable = false, name = "product_quantity")
    private Integer productQuantity;

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @OneToOne(targetEntity = ProductEntity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FkProduct", referencedColumnName = "id")
    private ProductEntity productEntity;

    @ManyToOne(targetEntity = OrderEntity.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FkPerson", nullable = false)
    private OrderEntity order;
}
