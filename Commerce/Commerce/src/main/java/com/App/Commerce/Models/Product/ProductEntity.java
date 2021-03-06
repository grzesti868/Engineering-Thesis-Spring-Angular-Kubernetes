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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    private String imgFile;

    @OneToMany(mappedBy="product", fetch = FetchType.LAZY)
    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<OrderDetailsEntity> orderDetails;

    public ProductEntity(String name, Money basePricePerUnit, Integer quantity, String imgFile) {
        this.name = name;
        this.basePricePerUnit = basePricePerUnit;
        this.quantity = quantity;
        this.imgFile = imgFile;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basePricePerUnit=" + basePricePerUnit +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedOn=" + updatedOn +
                ", imgFile='" + imgFile + '\'' +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
