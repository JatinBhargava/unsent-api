package com.unsent.api.service;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.dto.StoryContributionDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.DiaryEntry;
import com.unsent.api.entity.User;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.DiaryEntryRepository;
import com.unsent.util.CrudOperation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserService userService;
    private final SequenceService sequenceService;

    public DiaryEntryResponseDTO createEntry(DiaryEntryRequestDTO request) {
        User user = findUserEntityByUserId(request.getUserId());
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setUser(user);
        diaryEntry.setStoryId(sequenceService.generateStoryId());
        diaryEntry.setTitle(request.getTitle());
        diaryEntry.setContent(request.getContent());
        diaryEntry.setVisibility(request.getVisibility());
        diaryEntry.setStatus(request.getStatus());

        return toResponse(diaryEntryRepository.save(diaryEntry));
    }

    public List<DiaryEntryResponseDTO> getAllEntries() {
        return diaryEntryRepository.findLatestEntryOfEachStory().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DiaryEntryResponseDTO getEntryById(Long recordId) {
        return toResponse(findEntryById(recordId));
    }

    public List<DiaryEntryResponseDTO> getEntriesByUserId(String userId) {
        findUserByUserId(userId);
        return diaryEntryRepository.findByUserUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DiaryEntryResponseDTO updateEntry(Long recordId, DiaryEntryRequestDTO request) {
        DiaryEntry diaryEntry = findEntryById(recordId);
        User user = findUserEntityByUserId(diaryEntry.getUser().getUserId());
        DiaryEntry newEntry = new DiaryEntry();
        BeanUtils.copyProperties(diaryEntry,newEntry,"recordId");
        newEntry.setTitle(request.getTitle());
        newEntry.setUser(user);
        diaryEntry.setStoryId(sequenceService.generateStoryId());
        newEntry.setContent(request.getContent());
        newEntry.setVisibility(request.getVisibility());
        newEntry.setCrud_value(CrudOperation.UPDATE.getCode());
        return toResponse(diaryEntryRepository.save(newEntry));
    }

    public void deleteEntry(Long recordId) {
        DiaryEntry diaryEntry = findEntryById(recordId);
        diaryEntryRepository.delete(diaryEntry);
    }

    public long countEntriesByUserId(String userId) {
        findUserByUserId(userId);
        return diaryEntryRepository.countByUserUserId(userId);
    }

    public List<DiaryEntryResponseDTO> searchEntries(String userId, String keyword) {
        findUserByUserId(userId);
        return diaryEntryRepository.findByUserUserIdAndContentContainingIgnoreCase(userId, keyword).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DiaryEntry getLatestRecordIByStoryId(String storyId){
        return diaryEntryRepository.findTopByStoryIdOrderByRecordIdDesc(storyId);
    }

//    public StoryContributionDTO inScribeRequest(Long recordId, DiaryEntryRequestDTO request){
//
//        StoryContributionDTO storyContribution = storyContributionService.submitStoryContribution(recordId,request);
//        // if accept call inscribe method and insert in collab table with accepted
//        // if reject insert in collab table with rejected
//        // if withdrawn remove from collab table
//        return storyContribution;
//    }

    public DiaryEntryResponseDTO inscribe(Long recordId, DiaryEntryRequestDTO request){
        DiaryEntry inscribeDiary = findEntryById(recordId);
        String content =  inscribeDiary.getContent() + " " + request.getContent();
        DiaryEntry newEntry = new DiaryEntry();
        BeanUtils.copyProperties(inscribeDiary,newEntry,"recordId");
        newEntry.setContent(content);
        newEntry.setCrud_value(CrudOperation.UPDATE.getCode());
        return toResponse(diaryEntryRepository.save(newEntry));
    }



    public DiaryEntry findEntryById(Long recordId) {
        return diaryEntryRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Diary entry not found: " + recordId));
    }

    private Optional<User> findUserByUserId(String userId) {
       return userService.findByUserIdEntity(userId);
    }

    private DiaryEntryResponseDTO toResponse(DiaryEntry diaryEntry) {
        return new DiaryEntryResponseDTO(diaryEntry);
    }

    private User findUserEntityByUserId(String userId) {
        return userService
                .findByUserIdEntity(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found: " + userId
                        ));

    }
}
