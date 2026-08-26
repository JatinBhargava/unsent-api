package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class PostcardRequestDTO {

    @JsonProperty("toEmail")
    @NotBlank(message = "Add an email address on the front")
    @Email(message = "That email address does not look right")
    private final String toEmail;

    @JsonProperty("toName")
    @Size(max = 80, message = "Recipient name is too long")
    private final String toName;

    @JsonProperty("fromName")
    @Size(max = 80, message = "Sender name is too long")
    private final String fromName;

    /**
     * Sent by the client for convenience only. The authenticated principal on the
     * request is what the service actually trusts.
     */
    @JsonProperty("senderId")
    private final String senderId;

    @JsonProperty("message")
    @NotBlank(message = "Write something on the back first")
    @Size(max = 1200, message = "A postcard only holds 1200 characters")
    private final String message;
}
