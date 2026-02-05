package com.unsent.api.service;

import com.unsent.api.config.JWTUtil;
import com.unsent.api.dto.RegisterRequestDTO;
import com.unsent.api.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthService(final UserService userService, final PasswordEncoder passwordEncoder,
                       final JWTUtil jwtUtil){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(final RegisterRequestDTO request){
         if(userService.findByEmail(request.getEmail()).isPresent()){
             throw new RuntimeException("User Email ID already present.");
         }
          String hashedPassword = passwordEncoder.encode(request.getPassword());
          userService.saveUser(request.getEmail(),hashedPassword);
          return jwtUtil.generateToken(request.getEmail());
    }

    public String loginUser(final RegisterRequestDTO request){
        User user = validateUserCredentials(request);
        return jwtUtil.generateToken(user.getEmail());
    }

    private User validateUserCredentials(final RegisterRequestDTO request){
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        User user = optionalUser.orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if(!passwordEncoder.matches(request.getPassword(), user.getHashedPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

}
