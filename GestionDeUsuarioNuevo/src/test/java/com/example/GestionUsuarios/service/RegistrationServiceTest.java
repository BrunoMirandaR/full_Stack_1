package com.example.GestionUsuarios.service;

import com.example.GestionUsuarios.auth.AuthResponse;
import com.example.GestionUsuarios.auth.RegisterRequest;
import com.example.GestionUsuarios.jwt.JwtService;
import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .nombre("Juan")
                .appaterno("Pérez")
                .apmaterno("Gomez")
                .rut("12345678-9")
                .username("juanp")
                .password("1234")
                .idRol(1)
                .build();

        String encodedPassword = "encoded1234";
        String token = "jwt-token";

        when(passwordEncoder.encode("1234")).thenReturn(encodedPassword);
        when(jwtService.getToken(any(User.class))).thenReturn(token);

        AuthResponse response = registrationService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);

        verify(userRepository).save(any(User.class));
        verify(jwtService).getToken(any(User.class));
    }
}
