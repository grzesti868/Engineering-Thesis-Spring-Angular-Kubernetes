/*
 * Copyright (c) 2022.
 * @author Grzegorz Stich
 * @version 1.0
 */

package com.App.Commerce.Enums;


public enum OrderStatusEnum {
    New("New"), Confirmed("Confirmed"), Cancelled("Cancelled"), Pending("Pending") ;

    private final String code;

    OrderStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
