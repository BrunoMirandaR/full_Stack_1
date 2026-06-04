package com.example.Autentication.controller;

import com.example.Autentication.auth.AuthResponse;
import com.example.Autentication.auth.LoginRequest;
import com.example.Autentication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("usuario", "clave123");
        AuthResponse mockResponse = new AuthResponse("token-jwt");


        when(authService.login(loginRequest)).thenReturn(mockResponse);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);
    }

    @Test
    void testLoginFailure() {
        LoginRequest loginRequest = new LoginRequest("usuario", "claveIncorrecta");
        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Credenciales inválidas"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).isEqualTo("Credenciales inválidas");
    }
}
