package com.java.backend.onboarding_requirement.login;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.onboarding_requirement.security.dto.SignRequestDto;
import com.java.backend.onboarding_requirement.security.userdetails.UserDetailsImpl;
import com.java.backend.onboarding_requirement.security.userdetails.UserDetailsServiceImpl;
import com.java.backend.onboarding_requirement.user.domain.User;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import com.java.backend.onboarding_requirement.user.domain.UserRole;
import com.java.backend.onboarding_requirement.user.dto.SignUpRequestDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;  // 사용자 정보를 조회하는 서비스 Mocking

    private SignRequestDto signRequestDto;

    @BeforeEach
    public void setup() {
        signRequestDto = new SignRequestDto();
        signRequestDto.setUsername("JIN HO");
        signRequestDto.setPassword("12341234");

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("JIN HO");
        signUpRequestDto.setPassword("12341234");
        signUpRequestDto.setNickname("Mentos");

        String password = passwordEncoder.encode("12341234");

        User user = User.convertSignUpRequestDtoToUser(signUpRequestDto, password);
        UserAuthority userAuthority = new UserAuthority(UserRole.USER, user);
        user.setRoles(List.of(userAuthority));

        // 사용자 정보가 필요할 때 반환될 Mock 데이터 설정
        UserDetailsImpl mockUser = new UserDetailsImpl(user);
        Mockito.when(userDetailsService.loadUserByUsername("JIN HO")).thenReturn(mockUser);
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // 로그인 요청을 JSON 형식으로 변환
        String loginRequestJson = objectMapper.writeValueAsString(signRequestDto);

        // MockMvc로 로그인 요청 수행
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestJson));

        // 성공적인 로그인 응답 검증
        resultActions.andExpect(status().isOk());
    }

}
