package com.unsent.api.dto;
import com.unsent.util.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
