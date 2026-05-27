package com.unsent.api.service;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.entity.DiaryEntry;
import com.unsent.api.entity.User;
import com.unsent.api.repository.DiaryEntryRepository;
import com.unsent.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryEntryRepository diaryEntryRepository, UserRepository userRepository) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.userRepository = userRepository;
    }

    public DiaryEntryResponseDTO createEntry(DiaryEntryRequestDTO request) {
        User user = findUserByUserId(request.getUserId());

        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setUser(user);
        diaryEntry.setTitle(request.getTitle());
        diaryEntry.setContent(request.getContent());
        diaryEntry.setVisibility(request.getVisibility());
        diaryEntry.setStatus(request.getStatus());

        return toResponse(diaryEntryRepository.save(diaryEntry));
    }

    public List<DiaryEntryResponseDTO> getAllEntries() {
        return diaryEntryRepository.findAllByOrderByHostTsDesc().stream()
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

    public DiaryEntryResponseDTO updateEntry(Long recordId, String content, String visibility) {
        DiaryEntry diaryEntry = findEntryById(recordId);
        diaryEntry.setContent(content);
        diaryEntry.setVisibility(visibility);
        return toResponse(diaryEntryRepository.save(diaryEntry));
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

    private DiaryEntry findEntryById(Long recordId) {
        return diaryEntryRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Diary entry not found: " + recordId));
    }

    private User findUserByUserId(String userId) {
        return userRepository.findTopByUserIdOrderByRecordIdDesc(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private DiaryEntryResponseDTO toResponse(DiaryEntry diaryEntry) {
        return new DiaryEntryResponseDTO(diaryEntry);
    }
}
