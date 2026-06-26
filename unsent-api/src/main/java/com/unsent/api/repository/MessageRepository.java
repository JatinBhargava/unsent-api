package com.unsent.api.repository;

import com.unsent.api.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByConversationIdOrderByHostTsAsc(String conversationId);

}
