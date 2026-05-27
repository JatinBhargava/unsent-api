    package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
import com.unsent.util.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Id
    @SequenceGenerator(name = "record_seq", sequenceName = "record_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "record_seq")
    @Column(name = "record_id", nullable = false, updatable = false)
    private Long recordId;

    @Column(nullable = false)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String username;

    @Column(name = "display_name")
    private String displayName;

    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public User(
            String email,
            String hashedPassword,
            String username,
            String displayName,
            Gender gender,
            LocalDate dateOfBirth
    ) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.username = username;
        this.displayName = displayName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
