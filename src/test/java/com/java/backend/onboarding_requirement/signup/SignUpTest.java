package com.java.backend.onboarding_requirement.signup;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import com.java.backend.onboarding_requirement.user.dto.SignUpResponseDto;
import com.java.backend.onboarding_requirement.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private SignUpRequestDto signUpRequestDto;

    private static final String TEST_USERNAME = "JIN HO";
    private static final String TEST_PASSWORD = "12341234";
    private static final String TEST_NICKNAME = "Mentos";

    @BeforeEach
    void setUp() {
        signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername(TEST_USERNAME);
        signUpRequestDto.setPassword(TEST_PASSWORD);
        signUpRequestDto.setNickname(TEST_NICKNAME);
    }

    @Test
    @WithMockUser
    void signUp_Success() throws Exception {

        User user = getUser();

        when(userRepository.save(any())).thenReturn(user);

        SignUpResponseDto expectedResponse = SignUpResponseDto.convertUserToSignUpResponseDto(user);

        String signupRequestJson = objectMapper.writeValueAsString(signUpRequestDto);

        // MockMvc로 로그인 요청 수행
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupRequestJson));

        // 성공적인 로그인 응답 검증
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.username").value(TEST_USERNAME));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Mentos"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.authorities[0].authorityName").value("ROLE_USER"));
    }

    private User getUser() {
        String password = passwordEncoder.encode(TEST_PASSWORD);

        User user = User.convertSignUpRequestDtoToUser(signUpRequestDto, password);
        UserAuthority userAuthority = new UserAuthority(UserRole.USER, user);
        user.setRoles(List.of(userAuthority));
        return user;
    }
}
