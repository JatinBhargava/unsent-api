package com.unsent.api.repository;

import com.unsent.api.entity.NotificationSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationSubscriptionRepository extends JpaRepository<NotificationSubscription,Long> {
    boolean existsByEmailIgnoreCase(String email);
    NotificationSubscription findByEmailIgnoreCase(String email);
    List<NotificationSubscription> findByStatus(String status);
}
