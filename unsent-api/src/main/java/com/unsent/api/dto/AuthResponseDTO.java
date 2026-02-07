package com.unsent.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class AuthResponseDTO {

    private String token;

    public AuthResponseDTO(final String token){
        this.token = token;
    }
}
