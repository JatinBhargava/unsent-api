package com.unsent.api.controller;

import com.unsent.api.dto.AuthResponseDTO;
import com.unsent.api.dto.RegisterRequestDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.User;
import com.unsent.api.service.AuthService;
import com.unsent.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
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

    @GetMapping("/user")
    public Optional<User> getUserByEmail(@RequestParam("email") String email){
        final Optional<User> user = userService.findByEmail(email);
        return user;
    }

    @GetMapping("/user/{userId}")
    public Optional<User> getUserByUserId(@PathVariable("userId") String userId){
        final Optional<User> user = userService.findByUserId(userId);
        return user;
    }

    @PutMapping("/user/{userId}/profile")
    public void updateProfile(@PathVariable("userId") String userId,
                                                 @RequestBody UserDTO request){
         userService.updateProfile(userId,request);
    }
}
