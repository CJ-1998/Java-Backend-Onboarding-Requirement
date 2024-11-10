package com.java.backend.onboarding_requirement.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequestDto {
    private String username;
    private String password;
}
