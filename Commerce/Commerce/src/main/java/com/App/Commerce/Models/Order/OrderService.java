/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Enums.OrderStatusEnum;
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
    OrderEntity removeProductFromOrder(final Long orderDetailId, final Long orderId);
    Long addProductToOrder(final ProductEntity product, final Long orderId); //return order id
/*    void ValidateOrder(final Product Entity); //todo:?

    ProductEntity getProduct(final Long id);
    ProductEntity getProduct(final String name);
    void validateOrder(final OrderEntity product);
    update(final String name, final ProductEntity updateProduct);
    void deleteById(final Long id);
    void deleteByName(final String name);
    */

}
