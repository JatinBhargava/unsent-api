package com.unsent.api.dto;

import com.unsent.util.FriendRequestStatus;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDTO {

    private String sender_id;
    private String receiver_id;
    private String request_status;
}
