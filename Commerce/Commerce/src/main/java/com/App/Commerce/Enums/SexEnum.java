package com.App.Commerce.Enums;

public enum SexEnum {
    Male("M"), Female("F");

    private final String code;

    SexEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
