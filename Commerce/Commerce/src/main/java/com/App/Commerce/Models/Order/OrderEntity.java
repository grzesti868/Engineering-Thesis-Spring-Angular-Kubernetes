/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Enums.OrderStatusEnum;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;

    @Column(nullable = false, name = "product_quantity")
    private Integer productQuantity;

    @Column(nullable = false, name = "order_status")
    private OrderStatusEnum status;

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @ManyToOne(targetEntity = AppUserEntity.class)
    @JoinColumn(name="user_id", nullable=true)
    @JsonManagedReference
    private AppUserEntity buyer;

    @OneToMany(mappedBy="order")
    private Set<OrderDetailsEntity> orderDetails;

    public OrderEntity(Integer productQuantity, OrderStatusEnum status, AppUserEntity buyer) {
        this.productQuantity = productQuantity;
        this.status = status;
        this.buyer = buyer;
    }
}
