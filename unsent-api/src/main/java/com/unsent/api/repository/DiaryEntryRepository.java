package com.unsent.api.repository;

import com.unsent.api.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserUserId(String userId);

    List<DiaryEntry> findByUserUserIdAndContentContainingIgnoreCase(String userId, String keyword);

    long countByUserUserId(String userId);

    @Query(value = "SELECT d.* FROM diary_entries d INNER JOIN " +
            "(SELECT story_id, MAX(record_id) AS max_record_id FROM diary_entries " +
            "GROUP BY story_id) latest ON d.story_id = latest.story_id AND " +
            "d.record_id = latest.max_record_id ORDER BY d.record_id DESC", nativeQuery = true)
    List<DiaryEntry> findLatestEntryOfEachStory();

    DiaryEntry findTopByStoryIdOrderByRecordIdDesc(String storyId);
}
