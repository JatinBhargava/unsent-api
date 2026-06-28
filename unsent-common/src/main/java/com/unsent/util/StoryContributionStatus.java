package com.unsent.util;

import lombok.Getter;

@Getter
public enum StoryContributionStatus {
    PENDING("SCS100"),
    ACCEPTED("SCS101"),
    REJECTED("SCS102"),
    WITHDRAWN("SCS103"),
    EXPIRED("SCS104");

    private final String code;
    StoryContributionStatus(String code) {
        this.code = code;
    }
}
