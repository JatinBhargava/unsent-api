package com.unsent.util;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    MALE("M"),
    FEMALE("F"),
    NON_BINARY("N-B"),
    PREFER_NOT_TO_SAY("PNTS");

    private final String code;
    Gender(String code) {
        this.code = code;
    }

    public String toValue() {
        return code;
    }

    public static Gender fromValue(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(gender -> gender.code.equalsIgnoreCase(value) || gender.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid gender value: " + value));
    }
}
