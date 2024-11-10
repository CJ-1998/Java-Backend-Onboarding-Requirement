package com.java.backend.onboarding_requirement.user.dto;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import java.util.ArrayList;
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
    private List<UserAuthority> authorities;

    @Getter
    public static class UserAuthority {
        private String authorityName;

        public UserAuthority(String authorityName) {
            this.authorityName = authorityName;
        }
    }

    public static SignUpResponseDto converUserToSignUpResponseDto(User user) {

        List<UserAuthority> authorities = new ArrayList<>();
        for (UserRole role : user.getRoles()) {
            authorities.add(new UserAuthority(role.getAuthority()));
        }

        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorities(authorities)
                .build();
    }
}
