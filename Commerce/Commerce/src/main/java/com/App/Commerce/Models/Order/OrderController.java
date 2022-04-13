/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Enums.OrderStatusEnum;
import com.App.Commerce.Models.Product.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderEntity>> listAllOrders() {
        final List<OrderEntity> orderList = orderService.getAll();

        return ResponseEntity.ok().body(orderList);
    }

    @GetMapping("{username}/all")
    public ResponseEntity<List<OrderEntity>> getOrdersByUsername(@PathVariable final String username) {
        return ResponseEntity.ok().body(orderService.getOrdersByUsername(username));
    }

    //todo: test
    @GetMapping("/status/{orderStatusEnum}")
    public ResponseEntity<List<OrderEntity>> getOrdersByStatus(@PathVariable final OrderStatusEnum orderStatusEnum) {
        return ResponseEntity.ok().body(orderService.getAllByStatus(orderStatusEnum));
    }

    @GetMapping("/id/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable final Long orderId) {
        return ResponseEntity.ok().body(orderService.getOrderById(orderId));
    }
    @DeleteMapping("{orderId}")
    public ResponseEntity<String> deleteProductByProductName(@PathVariable("orderId") final Long orderId){
        orderService.deleteOrderById(orderId);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/orders/{orderId}}").toUriString());
        return ResponseEntity.ok().body("Order "+orderId+" has been deleted.");
    }

    @DeleteMapping("{orderId}/{OrderDetailsId}")
    public ResponseEntity<String> deleteOrderDetailFromOrder(
            @PathVariable("orderId") final Long orderId,
            @PathVariable("OrderDetailsId") final Long orderDetailsId
    ){
        orderService.removeProductFromOrder(orderDetailsId, orderId);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/orders/{orderId}/{OrderDetailsId}").toUriString());
        return ResponseEntity.ok().body("Order details "+ orderDetailsId + " from order "+ orderDetailsId +" has been deleted.");
    }
    @PostMapping("/{username}/new")
    public ResponseEntity<String> createNewOrderForUser(@PathVariable final String username) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/orders/{username}/new}").toUriString());
        return ResponseEntity.created(uri).body("New Order has been created for user: "+ orderService.newOrderForUser(username));
    }

    //updateOrder();
    //TODO: HERE, jak zaimplementowac update koszyka?
    @PutMapping("/update/{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable final String orderId,
            @RequestBody final OrderEntity order) {
        return ResponseEntity.ok("Order " + orderId + "has been updated, id: " + orderService.update(orderId, order));
    }

    /* OrderEntity addOrderDetailToOrder(final OrderDetailsEntity orderDetails, final Long orderId); //return order id
    Long addOrder(final OrderEntity orderEntity);
*/

    /*

    @GetMapping("{productname}")
    public ResponseEntity<ProductEntity> getProductByName(@PathVariable final String productname) {
        return ResponseEntity.ok().body(productService.getProduct(productname));
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
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/products/add").toUriString());
        return ResponseEntity.ok().body("Product "+productname+" has been deleted.");
    }*/
}
