package com.java.backend.onboarding_requirement.user.service;

import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import com.java.backend.onboarding_requirement.user.dto.SignUpResponseDto;
import com.java.backend.onboarding_requirement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        User user = User.convertSignUpRequestDtoToUser(signUpRequestDto);

        userRepository.save(user);


    }
}
