package com.unsent.util;

import lombok.Getter;

@Getter
public enum CrudOperation {
    CREATE("C"), UPDATE("U"), DELETE("D"), READ("R");

    private final String code;
    CrudOperation(String code) {
        this.code = code;
    }
}
