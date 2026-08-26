package com.unsent.api.controller;

import com.unsent.api.dto.PostcardRequestDTO;
import com.unsent.api.dto.PostcardResponseDTO;
import com.unsent.api.service.PostcardService;
import com.unsent.helper.BusinessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/postcard")
public class PostcardController {

    private final PostcardService postcardService;

    @PostMapping
    public PostcardResponseDTO send(
            @Valid @RequestBody PostcardRequestDTO request,
            Authentication authentication
    )
    {
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("PC003", "Sign in to send a postcard");
        }

        // JwtAuthFilter puts the token's email in the principal; the rate limit is
        // keyed on that rather than on any id the client sent us.
        return postcardService.send(request, authentication.getName());
    }
}
