/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;


import java.time.Period;
import java.util.List;

public interface ProductService {

    List<ProductEntity> getAll();
    Long addProduct(final ProductEntity product);
    ProductEntity update(final String name, final ProductEntity updateProduct);
    ProductEntity getProduct(final Long id);
    ProductEntity getProduct(final String name);
    void validateProductDetails(final ProductEntity product);
    void deleteById(final Long id);
    void deleteByName(final String name);
}
