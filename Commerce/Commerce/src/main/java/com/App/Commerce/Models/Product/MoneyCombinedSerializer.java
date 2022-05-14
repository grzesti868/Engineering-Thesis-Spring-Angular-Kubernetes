/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Models.Product;

import com.App.Commerce.Exceptions.ApiRequestException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.boot.jackson.JsonComponent;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@JsonComponent
@Slf4j
public class MoneyCombinedSerializer {
    public static class MoneyJsonSerializer extends JsonSerializer<Money> {


        @Override
        public void serialize(Money money,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("basePricePerUnit",
                    String.format(money.getNumber() + " " + money.getCurrency().toString())
            );
            jsonGenerator.writeEndObject();
        }
    }

    public static class MoneyJsonDeserializer extends JsonDeserializer<Money> {

        @Override
        public Money deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext)
                throws IOException, JacksonException {
            System.out.println("elo");
            String[] value = jsonParser.getValueAsString().split("\\s+");
            System.out.println(value.length);
            if(!isNumeric(value[0]) || value.length<=1)
                throw new ApiRequestException("Format of basePricePerUnit wrong! should be: <val> <currency>");

            CurrencyUnit currency = Monetary.getCurrency(value[1]);
            return Money.of(Integer.parseInt(value[0]), currency);
        }
    }

}
