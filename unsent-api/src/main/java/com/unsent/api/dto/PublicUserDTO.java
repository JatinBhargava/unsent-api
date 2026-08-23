package com.unsent.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicUserDTO {

    private String userId;

    private String username;

    private String displayName;
}
