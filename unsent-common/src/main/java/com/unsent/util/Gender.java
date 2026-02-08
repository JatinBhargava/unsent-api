package com.unsent.util;


import lombok.Getter;

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
}
