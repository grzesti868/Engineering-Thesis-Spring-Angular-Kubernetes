package com.App.Commerce.Enums;



public enum UserStatusEnum {
    Active("1"), Disabled("0");

    private final String code;

    UserStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
