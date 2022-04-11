/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderDetailsServiceImpl implements OrderDetailsService{

    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductService productService;

    @Override
    public OrderDetailsEntity getOrderDetails(Long id) {
        log.debug("Fetching order details by id: {}...", id);
        return orderDetailsRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Order details "+id +" not found!"));
    }



    @Override
    public Long createOrderDetails(OrderDetailsEntity orderDetails) {
        log.debug("Adding order details: {}", orderDetails.getId());
        validateOrderDetails(orderDetails);
        return orderDetailsRepository.save(orderDetails).getId();
    }

    @Override
    public void deleteOrderDetails(Long id) {
        log.debug("Deleting order detail {}", id);
        if (orderDetailsRepository.existsById(id))
            orderDetailsRepository.deleteById(id);
        else
            throw new ApiNotFoundException(String.format("Order detail by id: %d does not exists", id));
    }

    @Override
    public OrderDetailsEntity update(Long id, OrderDetailsEntity updateOrderDetails) {
        OrderDetailsEntity orderDetailToUpdate = orderDetailsRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Product to update does not exists"));

        Optional.ofNullable(updateOrderDetails.getOrder())
                .orElseThrow(() -> new ApiRequestException("Order entity can not be empty."));

        validateOrderDetails(updateOrderDetails);
        //todo: zostawic?
        orderDetailToUpdate.setProduct(updateOrderDetails.getProduct());
        orderDetailToUpdate.setProductQuantity(updateOrderDetails.getProductQuantity());
        //todo: zostawic?
        orderDetailToUpdate.setOrder(updateOrderDetails.getOrder());

        return orderDetailsRepository.save(orderDetailToUpdate);

    }

    @Override
    public void validateOrderDetails(OrderDetailsEntity orderDetails) {
        log.debug("Validating order detail...");

        Optional.ofNullable(orderDetails)
                .orElseThrow(() -> new ApiRequestException("Order detail can not be empty."));

        Optional.ofNullable(orderDetails.getProduct())
                .orElseThrow(() -> new ApiRequestException("Product can not be empty."));


        try {
            productService.getProduct(orderDetails.getProduct().getId());
        } catch (ApiNotFoundException err) {
            throw new ApiRequestException(
                    "Product "+ orderDetails.getProduct().getId()+" from order not found in database.");
        }


        log.debug("Product id {}",orderDetails.getProduct().getId());

        Optional.ofNullable(orderDetails.getProductQuantity())
                .orElseThrow(() -> new ApiRequestException("Quantity can not be empty."));

        Integer stock = productService.getProduct(orderDetails.getProduct().getId()).getQuantity();
        //todo: new exepction on how to hanlde empty stocks
        if (stock<orderDetails.getProductQuantity())
            throw new ApiRequestException("Not sufficient amount of product, in stock: "+stock);


        log.debug("Order details validated successfully!");
    }

    @Override
    public List<OrderDetailsEntity> getAllByOrder(Long orderId) {
        return orderDetailsRepository.findAllByOrderId(orderId);
    }
}
