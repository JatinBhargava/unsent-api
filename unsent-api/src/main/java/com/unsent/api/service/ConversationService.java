package com.unsent.api.service;

import com.unsent.api.dto.ChatMessageDTO;
import com.unsent.api.dto.ConversationDTO;
import com.unsent.api.entity.Conversation;
import com.unsent.api.helper.SequenceService;
import com.unsent.api.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService{

    private final ConversationRepository conversationRepository;
    private final SequenceService sequenceService;

    public Conversation findOrCreateConversation(ChatMessageDTO request){

        Optional<Conversation>  conversation = conversationRepository.findConversationBetweenUsers(request.getSenderId(),request.getReceiverId());

        if(conversation.isPresent()){
            return conversation.get();
        }else{
            return conversationRepository.save(saveConversationStatus(request));
        }
    }

    public Conversation saveConversationStatus(ChatMessageDTO request){
        Conversation conversation = Conversation.builder()
                .conversationId(sequenceService.nextConversationSequenceValue())
                .user1Id(request.getSenderId())
                .user2Id(request.getReceiverId())
                .status("active")
                .build();

        return conversation;
    }

    public ConversationDTO getConversationIdBetweenSenderandReciver(final String senderId , final String reciverId){

        Optional<Conversation>  conversation = conversationRepository.findConversationBetweenUsers(senderId,reciverId);

        return ConversationDTO.builder()
                .senderId(conversation.get().getUser1Id())
                .receiverId(conversation.get().getUser2Id())
                .conversationId(conversation.get().getConversationId())
                .build();
    }

}
