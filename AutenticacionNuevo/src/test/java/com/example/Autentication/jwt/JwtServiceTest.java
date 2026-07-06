package com.example.Autentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import com.example.Autentication.model.User;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void getTokenAndUsernameFromToken_shouldRoundTripUsername() {
        User user = User.builder()
                .username("usuario-prueba")
                .password("password")
                .build();

        String token = jwtService.getToken(user);

        assertThat(token).isNotBlank();
        assertThat(jwtService.getUsernameFromToken(token)).isEqualTo("usuario-prueba");
    }

    @Test
    void isTokenValid_shouldReturnTrueForMatchingUser() {
        User user = User.builder()
                .username("usuario-prueba")
                .password("password")
                .build();

        String token = jwtService.getToken(user);

        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {
        User user = User.builder()
                .username("usuario-prueba")
                .password("password")
                .build();
        User otherUser = User.builder()
                .username("otro-usuario")
                .password("password")
                .build();

        String token = jwtService.getToken(user);

        assertThat(jwtService.isTokenValid(token, otherUser)).isFalse();
    }
}