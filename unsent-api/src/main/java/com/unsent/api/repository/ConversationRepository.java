package com.unsent.api.repository;

import com.unsent.api.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation,Long> {
    @Query("SELECT c FROM Conversation c WHERE (c.user1Id = :user1Id AND c.user2Id = :user2Id) " +
            "OR (c.user1Id = :user2Id AND c.user2Id = :user1Id)")
    Optional<Conversation> findConversationBetweenUsers(@Param("user1Id") String user1Id,@Param("user2Id") String user2Id);
}
