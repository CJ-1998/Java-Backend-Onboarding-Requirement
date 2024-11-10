package com.java.backend.onboarding_requirement.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        String key = "6LcJCjCvxnX4tv6qji0bp6Aq7V9CMHeEIpTPOI693Oo=";
        byte[] bytes = Base64.getDecoder().decode(key);
        jwtUtil.setKey(Keys.hmacShaKeyFor(bytes));
    }

    @Test
    void AccessTokenTest() {
        String username = "kim";
        List<String> roles = List.of("ROLE_USER");

        String accessToken = jwtUtil.createAccessToken(username, roles);

        assertThat(accessToken).isNotNull();
    }

    @Test
    void RefreshTokenTest() {
        String username = "kim";

        String refreshToken = jwtUtil.createRefreshToken(username);

        assertThat(refreshToken).isNotNull();
    }

    @Test
    void verificationAccessTokenTest() {
        String username = "kim";
        List<String> roles = List.of("ROLE_USER");

        String accessToken = jwtUtil.createAccessToken(username, roles);
        String realAccessToken = jwtUtil.substringToken(accessToken);

        assertThat(jwtUtil.validateToken(realAccessToken)).isTrue();
    }

    @Test
    void verificationRefreshTokenTest() {
        String username = "kim";

        String refreshToken = jwtUtil.createRefreshToken(username);

        String realRefreshToken = jwtUtil.substringToken(refreshToken);

        assertThat(jwtUtil.validateToken(realRefreshToken)).isTrue();
    }

    @Test
    void wrongAccessTokenTest() {
        String username = "kim";
        List<String> roles = List.of("ROLE_USER");

        String accessToken = jwtUtil.createAccessToken(username, roles);
        String wrongAccessToken = jwtUtil.substringToken(accessToken) + "123";

        assertThatThrownBy(() -> jwtUtil.validateToken(wrongAccessToken))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void wrongRefreshTokenTest() {
        String username = "kim";

        String refreshToken = jwtUtil.createRefreshToken(username);

        String wrongRefreshToken = jwtUtil.substringToken(refreshToken) + "123";

        assertThatThrownBy(() -> jwtUtil.validateToken(wrongRefreshToken))
                .isInstanceOf(SignatureException.class);
    }
}