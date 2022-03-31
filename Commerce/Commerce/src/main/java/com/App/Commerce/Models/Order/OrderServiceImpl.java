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
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Product.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final AppUserService  appUserService;

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
        return null;
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        return null;
    }

    @Override
    public void deleteOrderById(Long id) {

    }

    @Override
    public OrderEntity removeProductFromOrder(Long orderDetailId, Long orderId) {
        return null;
    }

    @Override
    public Long addProductToOrder(ProductEntity product, Long orderId) {
        return null;
    }
}
