package com.unsent.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class RegisterRequestDTO {

    @JsonProperty("email")
    final String email;
    @JsonProperty("password")
    final String password;
    @JsonProperty("username")
    private final String username;
    @JsonProperty("displayName")
    private final String displayName;
    @JsonProperty("gender")
    private final String gender;
    @JsonProperty("date_of_birth")
    private final LocalDate dateOfBirth;
}
