package com.unsent.api.repository;

import com.unsent.api.entity.StoryContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryContributionRepository extends JpaRepository<StoryContribution,Long> {
    StoryContribution findByContributionId(String contributionId);
    List<StoryContribution> findByStoryIdAndStatus(String storyId, String status);

}
