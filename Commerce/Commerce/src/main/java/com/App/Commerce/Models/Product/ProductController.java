/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;

import com.App.Commerce.Models.AppUser.AppUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductEntity>> listAllProducts() {
        final List<ProductEntity> productList = productService.getAll();

        return ResponseEntity.ok().body(productList);
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductEntity> getProductByName(@PathVariable final Long productId) {
        return ResponseEntity.ok().body(productService.getProduct(productId));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody final ProductEntity product) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products/add").toUriString());
        return ResponseEntity.created(uri).body("Product has been added, Id: " + productService.addProduct(product));
    }

    @PutMapping("{productname}")
    public ResponseEntity<ProductEntity> updateProductByProductName(
            @PathVariable final String productname,
            @RequestBody final ProductEntity product) {
        return ResponseEntity.ok(productService.update(productname, product));
    }

    @DeleteMapping("{productname}")
    public ResponseEntity<String> deleteProductByProductName(@PathVariable("productname") final String productname){
        productService.deleteByName(productname);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products/{productname}").toUriString());
        return ResponseEntity.ok().body("Product "+productname+" has been deleted.");
    }

}
