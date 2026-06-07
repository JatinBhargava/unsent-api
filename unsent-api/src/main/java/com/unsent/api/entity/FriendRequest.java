package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import com.unsent.util.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friend_requests")
@NoArgsConstructor
@Getter
@Setter
public class FriendRequest extends BaseEntity {

    @Id
    @SequenceGenerator(name = "fr_record_seq", sequenceName = "fr_record_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fr_record_seq")
    @Column(name = "record_id", nullable = false, updatable = false)
    private Long recordId;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(name = "request_status", nullable = false)
    private String requestStatus;
}
