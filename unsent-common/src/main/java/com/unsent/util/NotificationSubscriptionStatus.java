package com.unsent.util;

public enum NotificationSubscriptionStatus {
    ACTIVE("ACTIVE"),
    UNSUBSCRIBED("UNSUBSCRIBED");

    private final String code;
    NotificationSubscriptionStatus(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}