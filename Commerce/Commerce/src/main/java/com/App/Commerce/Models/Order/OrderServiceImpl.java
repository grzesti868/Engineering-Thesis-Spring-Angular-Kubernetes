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
import com.App.Commerce.Models.OrderDetails.OrderDetailsService;

import com.App.Commerce.Models.Product.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void removeProductFromOrder(Long orderDetailId, Long orderId) {
        log.debug("Deleting order detail {} from order {}.", orderDetailId, orderId);
        if (orderRepository.existsById(orderId)) {
            try {
                log.debug("Order found. Removing order details...");
                OrderDetailsEntity orderDetail = orderDetailsService.getOrderDetails(orderDetailId);
                if(!Objects.equals(orderDetail.getOrder().getId(), orderId)) {
                    throw new ApiRequestException("Order details does not belong to order!");
                }
                orderDetailsService.deleteOrderDetails(orderDetailId);
                log.debug("Order details removed from order.");

            }
            catch (ApiNotFoundException err) {
                throw new ApiNotFoundException("Order does not contain "+ orderDetailId +" order details");
            }
        } else {
            throw new ApiNotFoundException(String.format("Order by id: %s does not exists.", orderId));
        }

    }

    @Override
    public OrderEntity addOrderDetailToOrder(OrderDetailsEntity orderDetails, Long orderId) {
        log.debug("Adding order detail to order {}", orderId);
        OrderEntity orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(()-> new ApiNotFoundException("Order: "+ orderId +" not found."));
        log.debug("Order found! Adding order details...");
        orderDetailsService.validateOrderDetails(orderDetails);


        Set<OrderDetailsEntity> orderDetailsToUpdate = orderToUpdate.getOrderDetails();
        orderDetailsToUpdate.forEach(order -> {
            if(order.getProduct().getId().equals(orderDetails.getProduct().getId())){
                throw new ApiRequestException("Product already in order!!!");
            }
        } );
        orderDetails.setOrder(orderToUpdate);
        orderDetailsToUpdate.add(orderDetails);
        Long id = orderDetailsService.createOrderDetails(orderDetails);
        orderToUpdate.setOrderDetails(orderDetailsToUpdate);
        return orderRepository.save(orderToUpdate);
    }
/*        log.info("Saving new user {} to database", user.getUsername() );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("User's password encoded: {}.", user.getPassword());

        if (appUserRepository.existsByUsername(user.getUsername()))
                throw new ApiRequestException("Username already exists.");

        if (appUserRepository.existsByEmail(user.getEmail()))
                throw new ApiRequestException("User's email already taken");

    validateUserDetails(user);

        personService.validatePersonDetails(user.getPersonEntity());

        return appUserRepository.save(user).getId();*/
    //todo: impl secure
    @Override
    public Long addOrder(OrderEntity order) {
        log.debug("Adding new order for user {}.",order.getBuyer().getUsername());
        return orderRepository.save(order).getId();
    }

    @Override
    public Long newOrderForUser(String username) {
        AppUserEntity user = appUserService.getUser(username);
        OrderEntity order = new OrderEntity(OrderStatusEnum.New, user, user.getPersonEntity().getAddressEntity());
        return addOrder(order);
    }

    @Override
    public Long update(Long orderId, OrderEntity updateOrder) {
        log.debug("Updating order {}...", orderId);
        OrderEntity orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiNotFoundException("Order to update does not exists."));

        if(orderToUpdate.getBuyer() != updateOrder.getBuyer() )
            throw new ApiRequestException("Order to update need to have the same buyer!");

        validateOrder(updateOrder);

        orderToUpdate.getOrderDetails()
                .forEach(orderDetailsService::validateOrderDetails);
        orderToUpdate.setOrderDetails(updateOrder.getOrderDetails());
        orderToUpdate.setAddressToSent(updateOrder.getAddressToSent());
        orderToUpdate.setStatus(updateOrder.getStatus());

        return orderRepository.save(orderToUpdate).getId();
    }

    @Override
    public void validateOrder(OrderEntity order) {
        log.debug("Validating order: {}",order.getId());

        Optional.ofNullable(order.getBuyer())
                .orElseThrow(() -> new ApiRequestException("Buyer can not be empty."));

        Optional.ofNullable(order.getAddressToSent())
                .orElseThrow(() -> new ApiRequestException("Address to sent not be empty."));

        Optional.ofNullable(order.getStatus())
                .orElseThrow(() -> new ApiRequestException("Order status can not be empty."));
        log.debug("Validated successfully!");
    }
}
