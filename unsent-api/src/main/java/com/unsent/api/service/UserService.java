package com.unsent.api.service;

import com.unsent.api.entity.User;
import com.unsent.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void saveUser(final String email, final String password, final String username,
                         final String displayName, final String gender, LocalDate dateOfBirth){
        User user = new User(email,password,username,displayName,gender,dateOfBirth);
        userRepository.save(user);
    }

    public Optional<User> findByEmail(final String email){
        return userRepository.findByEmail(email);
    }
}
