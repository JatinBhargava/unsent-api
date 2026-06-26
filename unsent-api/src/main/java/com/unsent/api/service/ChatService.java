package com.unsent.api.service;

import com.unsent.api.dto.ChatMessageDTO;
import com.unsent.api.dto.ConversationDTO;
import com.unsent.api.entity.Conversation;
import com.unsent.api.entity.Message;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.ConversationRepository;
import com.unsent.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SequenceService sequenceService;
    private final ConversationService conversationService;
    private final MessageService messageService;

    public void sendMessage(ChatMessageDTO request) {

        // 1. Validate friendship

        // 2. Find/Create Conversation
        conversationService.findOrCreateConversation(request);
        // 3. Save Message
        Message savedMessage = messageService.save(request);

        // 4. Push Message

        messagingTemplate.convertAndSendToUser(
                request.getReceiverId(),
                "/queue/messages",
                request
        );
    }

    public List<ChatMessageDTO> getMessagesOfConversation(String conversatiionId){
        return messageService.getConversationMessages(conversatiionId);
    }

    public ConversationDTO getConversationIdBetweenSenderandReciver(final String senderId , final String reciverId){
        return conversationService.getConversationIdBetweenSenderandReciver(senderId,reciverId);
    }
}