package com.unsent.api.controller;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.StoryContributionDTO;
import com.unsent.api.service.StoryContributionService;
import com.unsent.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diary/contribution")
@RequiredArgsConstructor
public class StoryContributionController {

    private final StoryContributionService storyContributionService;

    @PostMapping("/{recordId}/submit")
    public ApiResponse storyContribution(@PathVariable("recordId") Long recordId, @RequestBody DiaryEntryRequestDTO request) {

        StoryContributionDTO response = storyContributionService.submitStoryContribution(recordId, request);

        return ApiResponse.builder()
                .message("Story inscribed successfully")
                .count(1L)
                .data(response)
                .build();
    }

    @PostMapping("/{contributionId}/accept")
    public ApiResponse acceptStoryContribution(@PathVariable("contributionId") String contributionId) {

        StoryContributionDTO response = storyContributionService.acceptStoryContribution(contributionId);

        return ApiResponse.builder()
                .message("Contribution accepted!")
                .count(1L)
                .data(response)
                .build();
    }

    @PostMapping("/{contributionId}/reject")
    public ApiResponse rejectStoryContribution(@PathVariable("contributionId") String contributionId) {

        StoryContributionDTO response = storyContributionService.rejectStoryContribution(contributionId);

        return ApiResponse.builder()
                .message("Contribution rejected!")
                .count(1L)
                .data(response)
                .build();
    }

    @GetMapping("/{recordId}/pending")
    public ApiResponse getPendingContributions(@PathVariable("recordId") Long recordId) {

        List<StoryContributionDTO> contributions = storyContributionService.getPendingContributions(recordId);

        return ApiResponse.builder()
                .message("Pending story contributions fetched successfully")
                .count((long) contributions.size())
                .data(contributions)
                .build();
    }
}
