package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageDTO {

    private String senderId;

    private String receiverId;

    private String conversationId;

    private String messageText;

    private String messageId;

    private String createdAt;
}