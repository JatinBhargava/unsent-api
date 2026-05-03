package com.unsent.api.repository;

import com.unsent.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(final String email);
    Optional<User> findByEmailIgnoreCase(final String email);
    Optional<User> findByUsernameIgnoreCase(final String username);

    Optional<User> findByUserId(String userId);

    @Query(value = "SELECT user_id FROM users ORDER BY record_id DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLatestUserId();
}
