package com.unsent.api.repository;

import com.unsent.api.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserUserId(String userId);

    List<DiaryEntry> findByUserUserIdAndContentContainingIgnoreCase(String userId, String keyword);

    long countByUserUserId(String userId);

    @Query("""
    SELECT d
    FROM DiaryEntry d
    ORDER BY d.hostTs DESC
""")
    List<DiaryEntry> findLatestEntryOfEachUser();
}
