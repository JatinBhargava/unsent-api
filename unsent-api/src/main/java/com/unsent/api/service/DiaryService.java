package com.unsent.api.service;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.DiaryEntry;
import com.unsent.api.entity.User;
import com.unsent.api.repository.DiaryEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    private final DiaryEntryRepository diaryEntryRepository;
    private final UserService userService;

    public DiaryService(DiaryEntryRepository diaryEntryRepository, UserService userService) {
        this.diaryEntryRepository = diaryEntryRepository;
        this.userService = userService;
    }

    public DiaryEntryResponseDTO createEntry(DiaryEntryRequestDTO request) {
        User user = findUserEntityByUserId(request.getUserId());
        DiaryEntry diaryEntry = new DiaryEntry();
        diaryEntry.setUser(user);
        diaryEntry.setTitle(request.getTitle());
        diaryEntry.setContent(request.getContent());
        diaryEntry.setVisibility(request.getVisibility());
        diaryEntry.setStatus(request.getStatus());

        return toResponse(diaryEntryRepository.save(diaryEntry));
    }

    public List<DiaryEntryResponseDTO> getAllEntries() {
        return diaryEntryRepository.findLatestEntryOfEachUser().stream()
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
        User newUser = new User();
        BeanUtils.copyProperties(user,newUser);
        diaryEntry.setTitle(request.getTitle());
        diaryEntry.setUser(newUser);
        diaryEntry.setContent(request.getContent());
        diaryEntry.setVisibility(request.getVisibility());
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

    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .build();
    }
}
