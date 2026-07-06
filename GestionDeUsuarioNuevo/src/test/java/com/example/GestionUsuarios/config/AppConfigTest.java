package com.example.GestionUsuarios.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.GestionUsuarios.jwt.JwtService;

class AppConfigTest {

    private final AppConfig appConfig = new AppConfig();

    @Test
    void passwordEncoder_shouldReturnBcryptEncoder() {
        PasswordEncoder passwordEncoder = appConfig.passwordEncoder();

        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(passwordEncoder.encode("secret")).isNotBlank();
    }

    @Test
    void jwtService_shouldReturnJwtServiceInstance() {
        JwtService jwtService = appConfig.jwtService();

        assertThat(jwtService).isInstanceOf(JwtService.class);
    }
}