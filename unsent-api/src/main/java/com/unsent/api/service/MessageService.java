package com.unsent.api.service;

import com.unsent.api.dto.ChatMessageDTO;
import com.unsent.api.entity.Conversation;
import com.unsent.api.entity.Message;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final SequenceService sequenceService;
    private final ConversationService conversationService;
    private final MessageRepository messageRepository;

    public Message save(ChatMessageDTO request){

        Conversation conversation = conversationService.findOrCreateConversation(request);

        Message message = Message.builder()
                .messageId(sequenceService.nextMessageSequenceValue())
                .conversationId(conversation.getConversationId())
                .senderId(request.getSenderId())
                .messageText(request.getMessageText())
                .isRead(false)
                .status("active")
                .build();

        return messageRepository.save(message);
    }

    public List<ChatMessageDTO> getConversationMessages(
            String conversationId) {

        return messageRepository
                .findByConversationIdOrderByHostTsAsc(conversationId)
                .stream()
                .map(this::messageDTO)
                .toList();
    }

    private ChatMessageDTO messageDTO(Message message){
        return ChatMessageDTO.builder()
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .messageText(message.getMessageText()).build();
    }


}
