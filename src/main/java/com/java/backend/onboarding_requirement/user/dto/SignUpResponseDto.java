package com.java.backend.onboarding_requirement.user.dto;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SignUpResponseDto {

    private String username;
    private String nickname;
    private List<UserRole> authorities;

    public static SignUpResponseDto converUserToSignUpResponseDto(User user) {
        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorities(user.getRoles())
                .build();
    }
}
