package com.unsent.api.repository;

import com.unsent.api.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserUserId(String userId);

    List<DiaryEntry> findByUserUserIdAndContentContainingIgnoreCase(String userId, String keyword);

    long countByUserUserId(String userId);

    List<DiaryEntry> findAllByOrderByHostTsDesc();
}
