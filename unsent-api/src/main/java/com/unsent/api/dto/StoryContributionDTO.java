package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoryContributionDTO {
    private String contributionId;
    private String storyId;
    private String authorId;
    private String contributorId;
    private Long parentEntryId;
    private String content;
    private String status;
}
