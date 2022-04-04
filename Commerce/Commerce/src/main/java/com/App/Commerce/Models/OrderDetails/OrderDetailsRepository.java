/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity, Long> {
    //@Query("SELECT od FROM orders_details od WHERE od.FkUser.id = ?1")
    List<OrderDetailsEntity> findOrderDetailsOrderId(Long orderId);
}
