package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSubscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "subscription_id", nullable = false, unique = true)
    private String subscriptionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "status", nullable = false)
    private String status;
}