/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;

import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.javamoney.moneta.Money;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 25)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "base_price_per_unit")
    private Money basePricePerUnit;

    @Column(nullable = false, name = "quantity")
    private Integer Quantity;

    @Column(nullable = false, name = "timestamp", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date timestamp;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @Column(nullable = false, name = "image")
    //todo: how to impl images?
    private String ImgFile;

    @OneToMany(mappedBy="product", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<OrderDetailsEntity> orderDetails;

    public ProductEntity(String name, Money basePricePerUnit, Integer quantity, String imgFile) {
        this.name = name;
        this.basePricePerUnit = basePricePerUnit;
        Quantity = quantity;
        ImgFile = imgFile;
    }
}
