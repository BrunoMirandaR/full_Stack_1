package com.example.GestionUsuarios.controller;

import com.example.GestionUsuarios.auth.AuthResponse;
import com.example.GestionUsuarios.auth.RegisterRequest;
import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.service.RegistrationService;
import com.example.GestionUsuarios.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .username("nuevoUsuario")
                .password("password123")
                .build();

        AuthResponse mockResponse = AuthResponse.builder().token("jwt-token").build();

        when(registrationService.register(request)).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = registrationController.register(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockResponse);
    }

    @Test
    void testRegisterBadRequest() {
        RegisterRequest request = RegisterRequest.builder()
                .username(null)
                .password(null)
                .build();

        ResponseEntity<AuthResponse> response = registrationController.register(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().getToken()).contains("Error");
    }

    @Test
    void testGetUserByIdFound() {
        User user = User.builder().id(1).username("usuario").build();
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = registrationController.getUserById(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> response = registrationController.getUserById(99);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("Error: Usuario no encontrado.");
    }

    @Test
    void testUpdateUser() {
        User userToUpdate = User.builder().username("actualizado").build();
        User userUpdated = User.builder().id(1).username("actualizado").build();

        when(userService.updateUser(1, userToUpdate)).thenReturn(userUpdated);

        ResponseEntity<?> response = registrationController.updateUser(1, userToUpdate);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(userUpdated);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1);

        ResponseEntity<?> response = registrationController.deleteUser(1);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Usuario eliminado correctamente.");
    }

    @Test
    void testGetAllUsersSuccess() {
        List<User> users = Arrays.asList(
                User.builder().id(1).username("user1").build(),
                User.builder().id(2).username("user2").build());

        when(userService.mostrarUsuarios()).thenReturn(users);

        ResponseEntity<?> response = registrationController.getAllUsers();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(users);
    }

    @Test
    void testObtenerPorUsernameSuccess() {
        User user = User.builder().id(1).username("juan").build();

        when(userService.obtenerPorUsername("juan")).thenReturn(user);

        ResponseEntity<?> response = registrationController.obtenerPorUsername("juan");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    void testObtenerPorUsernameNotFound() {
        when(userService.obtenerPorUsername("noExiste"))
                .thenThrow(new RuntimeException("Usuario con username 'noExiste' no encontrado"));

        ResponseEntity<?> response = registrationController.obtenerPorUsername("noExiste");

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody().toString()).contains("noExiste");
    }
}
