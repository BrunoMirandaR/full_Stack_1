package com.example.Autentication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.Autentication.auth.AuthResponse;
import com.example.Autentication.auth.LoginRequest;
import com.example.Autentication.jwt.JwtService;
import com.example.Autentication.model.User;
import com.example.Autentication.repository.UserRepository;
import com.example.Autentication.repository.UsuarioConectadoRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsuarioConectadoRepository usuarioConectadoRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void TestLoginExitoso() {
        String username = "user1";
        String password = "password1";
        LoginRequest loginRequest = new LoginRequest(username, password);

        User user = User.builder()
                .id(1)
                .nombre("Prueba")
                .appaterno("Pérez")
                .apmaterno("Gómez")
                .rut("12345678-9")
                .username(username)
                .password("contraseñaCodificada")
                .id_rol("ROLE_USER")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, null, null));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        String mockToken = "mocked-jwt-token";
        when(jwtService.getToken(user)).thenReturn(mockToken);

        AuthResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(mockToken);

        // Verifica que se haya guardado el registro del usuario conectado con los valores esperados
        verify(usuarioConectadoRepository).save(
                org.mockito.ArgumentMatchers.argThat(uc ->
                        uc.getUserId().equals(user.getId()) &&
                        uc.getUsername().equals(user.getUsername()) &&
                        uc.getToken().equals(mockToken)
                )
        );
    }

    @Test
    void TestLoginUsuarioNoEncontrado() {
        String username = "usuarioNoExiste";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(username, password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, null, null));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertThat(thrown.getMessage()).isEqualTo("User not found");
    }
}