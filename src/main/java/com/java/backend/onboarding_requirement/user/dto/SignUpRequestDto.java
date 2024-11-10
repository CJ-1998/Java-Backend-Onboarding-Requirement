package com.java.backend.onboarding_requirement.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    private String username;
    private String password;
    private String nickname;
}
