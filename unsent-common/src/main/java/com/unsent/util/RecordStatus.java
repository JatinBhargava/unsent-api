package com.unsent.util;

import lombok.Getter;

@Getter
public enum RecordStatus {
    ACTIVE("RS100"),
    INACTIVE("RS101"),
    DELETED("RS102"),
    SUSPENDED("RS103"),
    DEACTIVATED("RS104");

    private final String code;
    RecordStatus(String code) {
        this.code = code;
    }
}
