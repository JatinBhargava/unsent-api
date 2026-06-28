package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "story_contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryContribution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "contribution_id", nullable = false, unique = true)
    private String contributionId;

    @Column(name = "story_id", nullable = false)
    private String storyId;

    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Column(name = "contributor_id", nullable = false)
    private String contributorId;

    @Column(name = "parent_entry_id", nullable = false)
    private Long parentEntryId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", nullable = false)
    private String status;
}