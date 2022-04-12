/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Long addProduct(ProductEntity product) {
        log.debug("Adding product: {}", product.getId());
        if (productRepository.existsByName(product.getName()))
            throw new ApiRequestException("Product already exists.");
        validateProductDetails(product);
        return productRepository.save(product).getId();
    }

    @Override
    public ProductEntity update(String name, ProductEntity updateProduct) {
        log.debug("Updating product {}...", name);
        ProductEntity productToUpdate = productRepository.findByName(name)
                .orElseThrow(() -> new ApiNotFoundException("Product to update does not exists"));

        validateProductDetails(updateProduct);

        productToUpdate.setBasePricePerUnit(updateProduct.getBasePricePerUnit());
        productToUpdate.setImgFile(productToUpdate.getImgFile());
        productToUpdate.setQuantity(productToUpdate.getQuantity());
        productToUpdate.setName(productToUpdate.getName());

        return productRepository.save(productToUpdate);

    }

    @Override
    public ProductEntity getProduct(Long id) {
        log.debug("Searching for product: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(String.format("Product %s was not found.", id)));
    }

    @Override
    public ProductEntity getProduct(String name) {
        log.debug("Searching for product: {}", name);
        return productRepository.findByName(name)
                .orElseThrow(() -> new ApiNotFoundException(String.format("Product %s was not found.", name)));
    }

    @Override
    public void validateProductDetails(ProductEntity product) {
        log.debug("Validating product: {}",product.getName());

        Optional.ofNullable(product)
                .orElseThrow(() -> new ApiRequestException("Product can not be empty."));

        Optional.ofNullable(product.getName())
                .orElseThrow(() -> new ApiRequestException("Name can not be empty."));

        Optional.ofNullable(product.getQuantity())
                .orElseThrow(() -> new ApiRequestException("Quantity can not be empty."));

        Optional.ofNullable(product.getBasePricePerUnit())
                .orElseThrow(() -> new ApiRequestException("Base price per unit can not be empty."));

        Optional.ofNullable(product.getImgFile())
                .orElseThrow(() -> new ApiRequestException("Image file can not be empty."));

        log.debug("Validated successfully!");
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting product {}", id);
        if (productRepository.existsById(id))
            productRepository.deleteById(id);
        else
            throw new ApiNotFoundException(String.format("Product by id: %s does not exists", id));
    }

    @Override
    public void deleteByName(String name) {
        log.debug("Deleting product {}", name);
        if (productRepository.existsByName(name)){
            productRepository.deleteByName(name);
            log.debug("Product {} has been deleted!", name);

        } else
            throw new ApiNotFoundException(String.format("Product by name: %s does not exists.", name));
    }
}
