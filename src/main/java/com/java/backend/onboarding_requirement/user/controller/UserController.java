package com.java.backend.onboarding_requirement.user.controller;

import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import com.java.backend.onboarding_requirement.user.dto.SignUpResponseDto;
import com.java.backend.onboarding_requirement.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "SignUp API", description = "회원 가입 관련 API.")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = userService.signUp(signUpRequestDto);
        return ResponseEntity.ok(signUpResponseDto);
    }
}
