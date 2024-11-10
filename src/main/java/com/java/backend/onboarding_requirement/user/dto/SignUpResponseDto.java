package com.java.backend.onboarding_requirement.user.dto;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
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
    private List<UserAuth> authorities;

    @Getter
    public static class UserAuth {
        private String authorityName;

        public UserAuth(String authorityName) {
            this.authorityName = authorityName;
        }
    }

    public static SignUpResponseDto converUserToSignUpResponseDto(User user) {

        List<UserAuth> authorities = new ArrayList<>();
        for (UserAuthority authority : user.getRoles()) {
            authorities.add(new UserAuth(authority.getRole().getAuthority()));
        }

        return SignUpResponseDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorities(authorities)
                .build();
    }
}
