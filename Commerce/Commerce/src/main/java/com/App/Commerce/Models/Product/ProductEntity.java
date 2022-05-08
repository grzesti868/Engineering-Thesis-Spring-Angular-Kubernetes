/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;

import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    @JsonDeserialize(using = MoneyCombinedSerializer.MoneyJsonDeserializer.class)
    private Money basePricePerUnit;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @Column(nullable = false, name = "created_at", updatable = false)
    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date createdAt;

    @Column(name = "updated_on")
    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    private Date updatedOn;

    @Column(nullable = false, name = "image")
    //todo: how to impl images?
    private String imgFile;

    @OneToMany(mappedBy="product", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private Set<OrderDetailsEntity> orderDetails;

    public ProductEntity(String name, Money basePricePerUnit, Integer quantity, String imgFile) {
        this.name = name;
        this.basePricePerUnit = basePricePerUnit;
        this.quantity = quantity;
        this.imgFile = imgFile;
    }
}
