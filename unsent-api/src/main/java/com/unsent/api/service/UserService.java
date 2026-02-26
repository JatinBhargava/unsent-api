package com.unsent.api.service;

import com.unsent.api.entity.User;
import com.unsent.api.repository.UserRepository;
import com.unsent.util.Gender;
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

    public User findOrCreate(String email) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setEmail(email);
                    user.setUsername(generateUsername(email));
                    user.setDisplayName("Anonymous");
                    user.setGender(Gender.PREFER_NOT_TO_SAY.getCode());
                    user.setDateOfBirth(null);
                    user.setHashedPassword(null); // OAuth users don’t need password
                    return userRepository.save(user);
                });
    }

    private String generateUsername(String email) {
        return email.split("@")[0] + "_" + System.currentTimeMillis();
    }
}
