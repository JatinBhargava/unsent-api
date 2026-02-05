package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class RegisterRequestDTO {

    @JsonProperty("email")
    final String email;
    @JsonProperty("password")
    final String password;
}
