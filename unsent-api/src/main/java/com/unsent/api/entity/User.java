package com.unsent.api.entity;

import com.unsent.entity.BaseEntity;
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

    @Column(nullable = false)
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String username;

    @Column(name = "display_name")
    private String displayName;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public User(
            String email,
            String hashedPassword,
            String username,
            String displayName,
            String gender,
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
