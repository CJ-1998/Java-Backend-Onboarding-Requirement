package com.java.backend.onboarding_requirement.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backend.onboarding_requirement.security.dto.SignRequestDto;
import com.java.backend.onboarding_requirement.security.dto.SignResponseDto;
import com.java.backend.onboarding_requirement.security.jwt.JwtUtil;
import com.java.backend.onboarding_requirement.security.userdetails.UserDetailsImpl;
import com.java.backend.onboarding_requirement.user.domain.UserAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/sign");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("로그인 시도");
        try {
            SignRequestDto signRequestDto = new ObjectMapper().readValue(request.getInputStream(),
                    SignRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signRequestDto.getUsername(),
                            signRequestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        List<UserAuthority> roles = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRoles();

        List<String> userRoles = new ArrayList<>();
        for (UserAuthority role : roles) {
            userRoles.add(role.getRole().getAuthority());
        }

        String accessToken = jwtUtil.createAccessToken(username, userRoles);
        jwtUtil.addJwtToHeader(accessToken, response);

        // 사용자 정보를 JSON으로 변환하여 response에 추가
        SignResponseDto signResponseDto = new SignResponseDto(jwtUtil.substringToken(accessToken));
        String userJsonResponse = new ObjectMapper().writeValueAsString(signResponseDto);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(userJsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }
}
