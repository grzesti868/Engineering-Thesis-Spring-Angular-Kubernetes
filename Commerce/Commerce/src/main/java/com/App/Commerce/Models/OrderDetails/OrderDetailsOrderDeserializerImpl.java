/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.OrderDetails;

import com.App.Commerce.Models.Order.OrderEntity;
import com.App.Commerce.Models.Order.OrderService;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
@Slf4j
@AllArgsConstructor
public class OrderDetailsOrderDeserializerImpl extends JsonDeserializer<OrderEntity> {

    private final OrderService orderService;

    @Override
    public OrderEntity deserialize(JsonParser jsonParser,
                                   DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        log.debug("Deserializable of Product Entity: {}", jsonParser.getText());
        return orderService.getOrderById(jsonParser.getValueAsLong());
    }
}
