package com.java.backend.onboarding_requirement.user.service;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import com.java.backend.onboarding_requirement.user.dto.SignUpResponseDto;
import com.java.backend.onboarding_requirement.user.repository.UserAuthorityRepository;
import com.java.backend.onboarding_requirement.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        User user = User.convertSignUpRequestDtoToUser(signUpRequestDto);
        user.setRoles(List.of(getUserAuthority(user)));
        userRepository.save(user);

        return SignUpResponseDto.convertUserToSignUpResponseDto(user);
    }

    private UserAuthority getUserAuthority(User user) {
        UserAuthority userAuthority = new UserAuthority(UserRole.USER, user);

        userAuthorityRepository.save(userAuthority);
        return userAuthority;
    }
}
