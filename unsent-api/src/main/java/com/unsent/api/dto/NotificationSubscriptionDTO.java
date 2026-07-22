package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class NotificationSubscriptionDTO {
    private String userId;
    private String email;
    private String subscriptionId;
    private String status;
}
