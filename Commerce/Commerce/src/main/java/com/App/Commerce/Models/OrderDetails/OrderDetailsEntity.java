/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Models.Order.OrderEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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

    @ManyToOne(targetEntity = ProductEntity.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_product", referencedColumnName = "id")
    private ProductEntity product;

    @ManyToOne(targetEntity = OrderEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_order", nullable = false, referencedColumnName = "id")
    private OrderEntity order;

    public OrderDetailsEntity(Integer productQuantity, ProductEntity product) {
        this.productQuantity = productQuantity;
        this.product = product;
    }
}
