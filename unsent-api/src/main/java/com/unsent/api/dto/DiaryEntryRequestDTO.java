package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class DiaryEntryRequestDTO {

    @JsonProperty("userId")
    private final String userId;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("content")
    private final String content;

    @JsonProperty("visibility")
    private final String visibility;

    @JsonProperty("status")
    private final String status;
}
