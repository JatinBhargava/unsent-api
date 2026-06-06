package com.unsent.util;

import lombok.Getter;

@Getter
public enum FriendRequestStatus {
    PENDING("FRS01"),
    ACCEPTED("FRS02"),
    REJECTED("FRS03");

    private final String code;
    FriendRequestStatus(String code) {
        this.code = code;
    }
}
