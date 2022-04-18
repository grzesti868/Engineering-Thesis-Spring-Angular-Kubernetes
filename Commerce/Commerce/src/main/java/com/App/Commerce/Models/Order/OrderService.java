/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Enums.OrderStatusEnum;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Set;

public interface OrderService {


    List<OrderEntity> getOrdersByUsername(final String username);
    List<OrderEntity> getAll();
    List<OrderEntity> getAllByStatus(OrderStatusEnum status);
    OrderEntity getOrderById(final Long id);
    void deleteOrderById(final Long id);
    void removeProductFromOrder(final Long orderDetailId, final Long orderId);
    OrderEntity addOrderDetailToOrder(final OrderDetailsEntity orderDetails, final Long orderId); //return order id
    Long addOrder(final OrderEntity orderEntity);
    Long newOrderForUser(final String username);
    Long update(final Long orderId,final OrderEntity order);

    void validateOrder(final OrderEntity order);


    /*
    void ValidateOrder(final OrderEntity order);
    ProductEntity getProduct(final Long id);
    ProductEntity getProduct(final String name);
    void validateOrder(final OrderEntity product);
    update(final String name, final ProductEntity updateProduct);
    void deleteById(final Long id);
    void deleteByName(final String name);
    */

}
