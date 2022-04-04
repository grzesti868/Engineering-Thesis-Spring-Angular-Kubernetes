/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Order;

import com.App.Commerce.Enums.OrderStatusEnum;
import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.AppUser.AppUserService;
import com.App.Commerce.Models.OrderDetails.OrderDetailsEntity;
import com.App.Commerce.Models.OrderDetails.OrderDetailsRepository;
import com.App.Commerce.Models.OrderDetails.OrderDetailsService;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final AppUserService  appUserService;
    private final OrderDetailsService orderDetailsService;

    @Override
    public List<OrderEntity> getOrdersByUsername(String username) {
        log.debug("Fetching username by : {}...", username);
        AppUserEntity user = appUserService.getUser(username);
        log.debug("User found, fetching orders...");
        List<OrderEntity> orders = Optional.ofNullable(user.getOrders())
                .orElseThrow(() -> new ApiRequestException("User has no orders."));
        log.debug("User has {} order(s).", orders.size());

        return orders;
    }

    @Override
    public List<OrderEntity> getAll() {
        log.debug("Fetching all orders...");
        return orderRepository.findAll();
    }

    @Override
    public List<OrderEntity> getAllByStatus(OrderStatusEnum status) {
        log.debug("Fetching orders by {} status.",status.getCode());
        return Optional.ofNullable(orderRepository.findAllByStatus(status))
                .orElseThrow(() -> new ApiNotFoundException(String.format("No orders with {} status", status.getCode())));
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        log.debug("Fetching order by {}.",id);
        return orderRepository.findById(id)
                .orElseThrow(()-> new ApiNotFoundException("No order by given id."));
    }

    @Override
    public void deleteOrderById(Long id) {
        log.debug("Deleting order {} from database.", id);
        if (orderRepository.existsById(id))
            orderRepository.deleteById(id);
        else
            throw new ApiNotFoundException(String.format("Order by id: %s does not exists.", id));
    }


    @Override
    public OrderEntity removeProductFromOrder(Long orderDetailId, Long orderId) {
        log.debug("Deleting product {} from order {}.", orderDetailId, orderId);
        if (orderRepository.existsById(orderId)) {
            try {
                OrderDetailsEntity orderDetail = orderDetailsService.getOrderDetails(orderDetailId);
                if(!Objects.equals(orderDetail.getOrder().getId(), orderId)) {
                    throw new ApiRequestException("Order details does not belong to order!");
                }
                orderDetailsService.deleteOrderDetails(orderDetailId);
            }
            catch (ApiNotFoundException err) {
                throw new ApiNotFoundException("Order detail and order does not match!");
            }
        } else {
            throw new ApiNotFoundException(String.format("Order by id: %s does not exists.", orderId));
        }
        return orderRepository.findById(orderId).get();
    }
    //todo: impl
    @Override
    public Long addProductToOrder(OrderDetailsEntity orderDetails, Long orderId) {
        return null;
    }
        //todo: impl
    @Override
    public void ValidateOrder(OrderEntity order) {

    }
}
