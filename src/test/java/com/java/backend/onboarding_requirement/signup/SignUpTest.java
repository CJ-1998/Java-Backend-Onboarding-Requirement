package com.java.backend.onboarding_requirement.signup;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.onboarding_requirement.user.controller.UserController;
import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import com.java.backend.onboarding_requirement.user.dto.SignUpResponseDto;
import com.java.backend.onboarding_requirement.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SignUpTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private SignUpRequestDto signUpRequestDto;

    @BeforeEach
    void setUp() {
        signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("JIN HO");
        signUpRequestDto.setPassword("12341234");
        signUpRequestDto.setNickname("Mentos");
    }

    @Test
    @WithMockUser
    void signUp_Success() throws Exception {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("JIN HO");
        signUpRequestDto.setPassword("12341234");
        signUpRequestDto.setNickname("Mentos");

        String password = passwordEncoder.encode("12341234");

        User user = User.convertSignUpRequestDtoToUser(signUpRequestDto, password);
        UserAuthority userAuthority = new UserAuthority(UserRole.USER, user);
        user.setRoles(List.of(userAuthority));

        when(userRepository.save(any())).thenReturn(user);

        SignUpResponseDto expectedResponse = SignUpResponseDto.convertUserToSignUpResponseDto(user);

        String signupRequestJson = objectMapper.writeValueAsString(signUpRequestDto);

        // MockMvc로 로그인 요청 수행
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequestJson));

        // 성공적인 로그인 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("JIN HO"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Mentos"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.authorities[0].authorityName").value("ROLE_USER"));
    }
}
