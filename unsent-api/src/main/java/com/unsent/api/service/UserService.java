package com.unsent.api.service;

import com.unsent.api.dto.UserDTO;
import com.unsent.api.entity.User;
import com.unsent.api.repository.UserRepository;
import com.unsent.util.CrudOperation;
import com.unsent.util.Gender;
import com.unsent.util.RecordStatus;
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
        user.setStatus(RecordStatus.ACTIVE.getCode());
        userRepository.save(user);
    }

    public UserDTO findByEmail(final String email) {

      Optional<User> user = userRepository
              .findTopByEmailIgnoreCaseOrderByRecordIdDesc(email.trim());

      return UserDTO.builder()
                .userId(user.get().getUserId())
                .username(user.get().getUsername())
                .displayName(user.get().getDisplayName())
                .gender(user.get().getGender())
                .dateOfBirth(user.get().getDateOfBirth())
                .build();

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
                    user.setStatus(RecordStatus.ACTIVE.getCode());
                    // Column is NOT NULL; store a random hash so OAuth-only users persist safely.
                    user.setHashedPassword(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
                    return userRepository.save(user);
                });
    }

    public UserDTO findByUserId(final String userId) {
        Optional<User> user = userRepository.findTopByUserIdOrderByRecordIdDesc(userId);
        return UserDTO.builder()
                .userId(user.get().getUserId())
                .username(user.get().getUsername())
                .displayName(user.get().getDisplayName())
                .gender(user.get().getGender())
                .dateOfBirth(user.get().getDateOfBirth())
                .build();
    }

    public Optional<User> findByUserIdEntity(final String userId) {
        Optional<User> user = userRepository.findTopByUserIdOrderByRecordIdDesc(userId);
        return user;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email.trim());
    }

    public void updateProfile(String userId, UserDTO request) {
        Optional<User> user = userRepository.findTopByUserIdOrderByRecordIdDesc(userId);
        user.get().setDisplayName(request.getDisplayName());
        user.get().setGender(request.getGender());
        user.get().setDateOfBirth(request.getDateOfBirth());
        userRepository.save(user.get());
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
