package com.unsent.api.dto;

import com.unsent.api.entity.DiaryEntry;
import lombok.Getter;

@Getter
public class DiaryEntryResponseDTO {

    private final Long recordId;
    private final String userId;
    private final String title;
    private final String content;
    private final String visibility;
    private final String status;

    public DiaryEntryResponseDTO(DiaryEntry diaryEntry) {
        this.recordId = diaryEntry.getRecordId();
        this.userId = diaryEntry.getUser().getUserId();
        this.title = diaryEntry.getTitle();
        this.content = diaryEntry.getContent();
        this.visibility = diaryEntry.getVisibility();
        this.status = diaryEntry.getStatus();
    }
}
