package com.unsent.api.repository;

import com.unsent.api.entity.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SequenceRepository extends JpaRepository<DiaryEntry,Long> {

    @Query(value = "SELECT nextval('story_id_seq')", nativeQuery = true)
    Long getNextStoryId();

    @Query(value = "SELECT nextval('messages_seq')", nativeQuery = true)
    String getNextMessageId();

    @Query(value = "SELECT nextval('conversation_seq')", nativeQuery = true)
    String getNextConversationId();

    @Query(value = "SELECT nextval('story_contributions_seq')", nativeQuery = true)
    String getNextStoryContributionId();

    @Query(value = "SELECT nextval('notification_subscriptions_seq')", nativeQuery = true)
    String getNotificationSubscriptionId();
}
