package com.unsent.api.controller;

import com.unsent.api.dto.AuthResponseDTO;
import com.unsent.api.dto.PublicUserDTO;
import com.unsent.api.dto.RegisterRequestDTO;
import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.User;
import com.unsent.api.service.AuthService;
import com.unsent.api.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request){
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
    public UserDTO getUserByEmail(@RequestParam("email") String email){
        final UserDTO user = userService.findByEmail(email);
        return user;
    }

    @GetMapping("/user/{userId}")
    public UserDTO getUserByUserId(@PathVariable("userId") String userId){
        final UserDTO user = userService.findByUserId(userId);
        return user;
    }

    // Non-sensitive profile info safe to show to other users (diary author
    // byline, friends list) — deliberately excludes gender/dateOfBirth/email.
    @GetMapping("/user/{userId}/public")
    public PublicUserDTO getPublicUserByUserId(@PathVariable("userId") String userId){
        return userService.findPublicByUserId(userId);
    }

    @PutMapping("/user/{userId}/profile")
    public void updateProfile(@PathVariable("userId") String userId,
                                                 @RequestBody UserDTO request){
         userService.updateProfile(userId,request);
    }
}
