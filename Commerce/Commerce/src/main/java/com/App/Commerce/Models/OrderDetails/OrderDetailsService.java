/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.Product.ProductEntity;

import java.util.List;

public interface OrderDetailsService {

    OrderDetailsEntity getOrderDetails(final Long id);
    Long createOrderDetails(final OrderDetailsEntity orderDetails);
    void deleteOrderDetails(final Long id);
    OrderDetailsEntity update(final Long id, final OrderDetailsEntity updateOrderDetails);
    void validateOrderDetails(final OrderDetailsEntity orderDetails);
    List<OrderDetailsEntity> getAllByOrder(final Long orderId);
}
