package com.unsent.api.service;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.dto.StoryContributionDTO;
import com.unsent.api.entity.DiaryEntry;
import com.unsent.api.entity.StoryContribution;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.StoryContributionRepository;
import com.unsent.util.StoryContributionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoryContributionService {

    private final SequenceService sequenceService;
    private final StoryContributionRepository storyContributionRepository;
    private final DiaryService diaryService;

    public StoryContributionDTO submitStoryContribution(final Long recordId, DiaryEntryRequestDTO request){

        DiaryEntryResponseDTO story = diaryService.getEntryById(recordId);

        StoryContribution storyContribution = StoryContribution.builder()
                .contributionId(sequenceService.nextStoryContributionSequenceValue())
                .storyId(story.getStoryId())
                .content(request.getContent())
                .authorId(story.getUserId())
                .contributorId(request.getUserId())
                .parentEntryId(recordId)
                .status(StoryContributionStatus.PENDING.getCode())
                .build();

        StoryContribution savedContribution = storyContributionRepository.save(storyContribution);
        return storyContibutionStructure(savedContribution);
    }

    public StoryContributionDTO acceptStoryContribution(final String contributionId){

        //isContributionExpired(diaryId);
        StoryContribution storyContribution = storyContributionRepository.findByContributionId(contributionId);
        storyContribution.setStatus(StoryContributionStatus.ACCEPTED.getCode());
        StoryContribution savedContribution = storyContributionRepository.save(storyContribution);

        DiaryEntryRequestDTO request = DiaryEntryRequestDTO.builder().content(savedContribution.getContent()).build();
        diaryService.inscribe(savedContribution.getParentEntryId(), request);
        return storyContibutionStructure(savedContribution);
    }

    public StoryContributionDTO rejectStoryContribution(final String contributionId){

        //isContributionExpired(diaryId);
        StoryContribution storyContribution = storyContributionRepository.findByContributionId(contributionId);
        storyContribution.setStatus(StoryContributionStatus.ACCEPTED.getCode());
        StoryContribution savedContribution = storyContributionRepository.save(storyContribution);
        return storyContibutionStructure(savedContribution);
    }

    public boolean isContributionExpired(final String contributionId){

        StoryContribution contribution = storyContributionRepository.findByContributionId(contributionId);

        DiaryEntry latestEntry = diaryService.getLatestRecordIByStoryId(contribution.getStoryId());

        return !latestEntry.getRecordId().equals(contribution.getParentEntryId());
    }

    @Cacheable(value = "pendingContribution", key="'all'")
    public List<StoryContributionDTO> getPendingContributions(Long recordId) {

        DiaryEntry diary = diaryService.findEntryById(recordId);

        return storyContributionRepository.findByStoryIdAndStatus(diary.getStoryId(), StoryContributionStatus.PENDING.getCode())
                .stream()
                .map(this::storyContibutionStructure)
                .toList();
    }

    private StoryContributionDTO storyContibutionStructure(StoryContribution contribution) {

        return StoryContributionDTO.builder()
                .contributionId(contribution.getContributionId())
                .storyId(contribution.getStoryId())
                .authorId(contribution.getAuthorId())
                .contributorId(contribution.getContributorId())
                .parentEntryId(contribution.getParentEntryId())
                .content(contribution.getContent())
                .status(contribution.getStatus())
                .build();
    }
}
