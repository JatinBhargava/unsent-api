package com.unsent.api.service;

import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.User;
import com.unsent.api.repository.UserRepository;
import com.unsent.util.CrudOperation;
import com.unsent.util.Gender;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(final String email, final String password, final String username,
                         final String displayName, final Gender gender, LocalDate dateOfBirth) {
        User user = new User(email, password, username, displayName, gender, dateOfBirth);
        user.setUserId(generateNextUserId());
        user.setCrud_value(CrudOperation.CREATE.getCode());
        userRepository.save(user);
    }

    public Optional<User> findByEmail(final String email) {
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.findTopByEmailIgnoreCaseOrderByRecordIdDesc(email.trim());
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
            return userRepository.findTopByEmailIgnoreCaseOrderByRecordIdDesc(normalizedIdentifier);
        }
        return userRepository.findByUsernameIgnoreCase(normalizedIdentifier);
    }

    public User findOrCreate(String email) {
        return userRepository.findTopByEmailIgnoreCaseOrderByRecordIdDesc(email)
                .orElseGet(() -> {
                    User user = new User();
                    user.setUserId(generateNextUserId());
                    user.setEmail(email.trim());
                    user.setUsername(generateUsername(email));
                    user.setDisplayName("Anonymous");
                    user.setGender(Gender.PREFER_NOT_TO_SAY);
                    user.setDateOfBirth(null);
                    user.setCrud_value(CrudOperation.CREATE.getCode());
                    // Column is NOT NULL; store a random hash so OAuth-only users persist safely.
                    user.setHashedPassword(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
                    return userRepository.save(user);
                });
    }

    public Optional<User> findByUserId(final String userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return userRepository.findTopByUserIdOrderByRecordIdDesc(userId);
    }

    public void updateProfile(String userId, UserDTO request) {
        Optional<User> user = userRepository.findTopByUserIdOrderByRecordIdDesc(userId);
        User newUser = new User();
        BeanUtils.copyProperties(user.get(),newUser,
                "recordId", "crud_value", "uuid");
        newUser.setRecordId(null);
        newUser.setDisplayName(request.getDisplayName());
        newUser.setGender(request.getGender());
        newUser.setDateOfBirth(request.getDateOfBirth());
        newUser.setUuid(UUID.randomUUID());
        newUser.setCrud_value(CrudOperation.UPDATE.getCode());
        userRepository.save(newUser);
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
