package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "friendships")
@NoArgsConstructor
@Getter
@Setter
public class Friend extends BaseEntity {

    @Id
    @SequenceGenerator(name = "fr_record_seq", sequenceName = "fr_record_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fr_record_seq")
    @Column(name = "record_id", nullable = false, updatable = false)
    private Long recordId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "friend_id", nullable = false)
    private String friendId;

}
