package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class PostcardResponseDTO {

    /**
     * A correlation id for logs and support requests. Nothing is stored against it —
     * a postcard exists only in the recipient's inbox.
     */
    @JsonProperty("postcardId")
    private final String postcardId;

    @JsonProperty("toEmail")
    private final String toEmail;

    @JsonProperty("sentAt")
    private final String sentAt;
}
