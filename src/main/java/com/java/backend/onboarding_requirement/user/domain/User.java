package com.java.backend.onboarding_requirement.user.domain;

import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "test_user")
@ToString
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long id;

    private String username;
    private String password;
    private String nickname;

    @ElementCollection
    private List<UserRole> roles;

    public static User convertSignUpRequestDtoToUser(SignUpRequestDto signUpRequestDto) {
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.USER);

        return User.builder()
                .username(signUpRequestDto.getUsername())
                .password(signUpRequestDto.getPassword())
                .nickname(signUpRequestDto.getNickname())
                .roles(roles)
                .build();
    }

}
