package com.unsent.api.controller;

import com.unsent.api.dto.DiaryEntryRequestDTO;
import com.unsent.api.dto.DiaryEntryResponseDTO;
import com.unsent.api.service.DiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ResponseEntity<DiaryEntryResponseDTO> createEntry(@RequestBody DiaryEntryRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(diaryService.createEntry(request));
    }

    @GetMapping
    public ResponseEntity<List<DiaryEntryResponseDTO>> getAllEntries() {
        return ResponseEntity.ok(diaryService.getAllEntries());
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<DiaryEntryResponseDTO> getEntryById(@PathVariable("recordId") Long recordId) {
        return ResponseEntity.ok(diaryService.getEntryById(recordId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiaryEntryResponseDTO>> getEntriesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(diaryService.getEntriesByUserId(userId));
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countEntriesByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(diaryService.countEntriesByUserId(userId));
    }

    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<DiaryEntryResponseDTO>> searchEntries(
            @PathVariable String userId,
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(diaryService.searchEntries(userId, keyword));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<DiaryEntryResponseDTO> updateEntry(
            @PathVariable("recordId") Long recordId,
            @RequestBody DiaryEntryRequestDTO request
    ) {
        return ResponseEntity.ok(diaryService.updateEntry(recordId,request));
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteEntry(@PathVariable("recordId") Long recordId) {
        diaryService.deleteEntry(recordId);
        return ResponseEntity.noContent().build();
    }
}
