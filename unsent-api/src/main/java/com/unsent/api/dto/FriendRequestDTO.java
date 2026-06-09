package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unsent.util.FriendRequestStatus;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class FriendRequestDTO {

    private String sender_id;
    private String receiver_id;
    private String request_status;

    public FriendRequestDTO(String request_status) {
        this.request_status = request_status;
    }
}
