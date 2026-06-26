package com.unsent.api.controller;

import com.unsent.api.dto.ChatMessageDTO;
import com.unsent.api.dto.ConversationDTO;
import com.unsent.api.service.ChatService;
import com.unsent.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/messages")
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO message) {
        chatService.sendMessage(message);
    }

    @GetMapping("/conversation/{conversationId}")
    public ApiResponse getConversationMessages(@PathVariable("conversationId") String conversationId) {

        List<ChatMessageDTO> messages = chatService.getMessagesOfConversation(conversationId);

        return ApiResponse.builder()
                .message("Message fetched successfully")
                .count((long) messages.size())
                .data(messages)
                .build();
    }

    @GetMapping("/conversation")
    public ApiResponse getConversationIdBetweenSenderandReciver(
            @RequestParam("senderId") final String senderId , @RequestParam("receiverId") final String reciverId){
        ConversationDTO conversation = chatService.getConversationIdBetweenSenderandReciver(senderId,reciverId);

        return ApiResponse.builder()
                .message("Conversation Relationship fetched successfully")
                .data(conversation)
                .build();
    }


}