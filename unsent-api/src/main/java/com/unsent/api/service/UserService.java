package com.unsent.api.service;

import com.unsent.api.entity.User;
import com.unsent.api.repository.UserRepository;
import com.unsent.util.Gender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void saveUser(final String email, final String password, final String username,
                         final String displayName, final Gender gender, LocalDate dateOfBirth){
        User user = new User(email,password,username,displayName,gender,dateOfBirth);
        user.setUserId(generateNextUserId());
        userRepository.save(user);
    }

    public Optional<User> findByEmail(final String email){
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.findByEmailIgnoreCase(email.trim());
    }

    public Optional<User> findByEmailOrUsername(final String identifier) {
        if (identifier == null) {
            return Optional.empty();
        }
        final String normalizedIdentifier = identifier.trim();
        if (normalizedIdentifier.isEmpty()) {
            return Optional.empty();
        }

        if (normalizedIdentifier.contains("@")) {
            return userRepository.findByEmailIgnoreCase(normalizedIdentifier);
        }
        return userRepository.findByUsernameIgnoreCase(normalizedIdentifier);
    }

    public User findOrCreate(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setUserId(generateNextUserId());
                    user.setEmail(email.trim());
                    user.setUsername(generateUsername(email));
                    user.setDisplayName("Anonymous");
                    user.setGender(Gender.PREFER_NOT_TO_SAY);
                    user.setDateOfBirth(null);
                    // Column is NOT NULL; store a random hash so OAuth-only users persist safely.
                    user.setHashedPassword(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
                    return userRepository.save(user);
                });
    }

    private String generateUsername(String email) {
        return email.split("@")[0] + "_" + System.currentTimeMillis();
    }

    private String generateNextUserId() {
        return String.valueOf(
                userRepository.findLatestUserId()
                        .map(this::incrementUserId)
                        .orElse(1L)
        );
    }

    private long incrementUserId(String currentUserId) {
        try {
            return Long.parseLong(currentUserId) + 1;
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("Existing userId is not numeric: " + currentUserId, exception);
        }
    }
}
