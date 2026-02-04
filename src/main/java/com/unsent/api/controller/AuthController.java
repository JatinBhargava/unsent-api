package com.unsent.api.controller;

import com.nimbusds.jose.crypto.impl.AAD;
import com.unsent.api.dto.AuthResponseDTO;
import com.unsent.api.dto.RegisterRequestDTO;
import com.unsent.api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody RegisterRequestDTO request){
            final String token = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody RegisterRequestDTO request){
        final String token = authService.loginUser(request);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
