package com.App.Commerce.Enums;



public enum StatusEnum {
    Active("1"), Disabled("0");

    private final String code;

    StatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
