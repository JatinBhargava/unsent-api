package com.unsent.api.dto;
import com.unsent.util.Gender;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String userId;

    private String email;

    private String username;

    private String displayName;

    private Gender gender;

    private LocalDate dateOfBirth;

    //update User Profile

    public UserDTO(String displayName, Gender gender, LocalDate dateOfBirth) {
        this.displayName = displayName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
