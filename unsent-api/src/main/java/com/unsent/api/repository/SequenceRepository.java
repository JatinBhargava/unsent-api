package com.unsent.api.repository;

import com.unsent.api.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SequenceRepository extends JpaRepository<DiaryEntry,Long> {

    @Query(value = "SELECT nextval('story_id_seq')", nativeQuery = true)
    Long getNextStoryId();

}
