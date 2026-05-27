package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diary_entries")
@Getter
@Setter
@NoArgsConstructor
public class DiaryEntry extends BaseEntity {

    @Id
    @SequenceGenerator(name = "record_seq", sequenceName = "record_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq")
    @Column(name = "record_id", nullable = false, updatable = false)
    private Long recordId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "record_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "text")
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column
    private String status;

    @Column
    private String visibility;
}
