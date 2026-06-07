package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContinueStoryDTO {

    @JsonProperty("recordId")
    private Long storyId;
    private String userId;
    private String generatedContent;

    public ContinueStoryDTO(Long storyId) {
        this.storyId = storyId;
    }

    public ContinueStoryDTO(String generatedContent) {
        this.generatedContent = generatedContent;
    }
}