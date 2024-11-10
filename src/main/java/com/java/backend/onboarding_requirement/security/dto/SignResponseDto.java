package com.java.backend.onboarding_requirement.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignResponseDto {
    private String token;

    public SignResponseDto(String token) {
        this.token = token;
    }
}
