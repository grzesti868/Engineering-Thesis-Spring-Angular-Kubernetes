/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Models.Product.ProductEntity;
import com.App.Commerce.Models.Product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order/details")
public class OrderDetailsController {

    private final OrderDetailsService orderDetailsService;

    @GetMapping("/by/order/{orderId}")
    public ResponseEntity<List<OrderDetailsEntity>> listAllOrderDetailsByOrder(@PathVariable final Long orderId) {
        final List<OrderDetailsEntity> orderDetailsList = orderDetailsService.getAllByOrder(orderId);
        System.out.println("TEST: "+ orderDetailsList.size());

        return ResponseEntity.ok().body(orderDetailsList);
    }

    @PutMapping("{orderID}")
    public ResponseEntity<OrderDetailsEntity> updateProductByProductName(
            @PathVariable final Long orderID,
            @RequestBody final OrderDetailsEntity OrderDetail) {
        return ResponseEntity.ok(orderDetailsService.update(orderID, OrderDetail));
    }

}
