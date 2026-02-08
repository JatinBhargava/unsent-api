package com.unsent.api.entity;

import entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "diary_entries")
@Getter
@NoArgsConstructor
public class DairyEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", referencedColumnName = "username",nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "text")
    private String content;
}
