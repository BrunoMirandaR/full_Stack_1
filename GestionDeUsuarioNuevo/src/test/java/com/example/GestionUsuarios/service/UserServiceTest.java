package com.example.GestionUsuarios.service;

import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserByIdFound() {
        User user = User.builder().id(1).username("juan").build();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1);

        assertThat(result).isPresent().contains(user);
    }
    
    @Test
    void testUpdateUserSuccess() {
        User original = User.builder().id(1).username("juan").build();
        User updated = User.builder().username("nuevo").nombre("Nuevo").idRol(2).build();

        when(userRepository.findByUsername("nuevo")).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(original));
        when(userRepository.save(any(User.class))).thenReturn(original);

        User result = userService.updateUser(1, updated);

        assertThat(result).isEqualTo(original);
    }

    @Test
    void testUpdateUserUsernameConflict() {
        User updated = User.builder().username("repetido").build();
        User conflictUser = User.builder().id(2).username("repetido").build();

        when(userRepository.findByUsername("repetido")).thenReturn(Optional.of(conflictUser));

        Throwable thrown = catchThrowable(() -> userService.updateUser(1, updated));

        assertThat(thrown)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("El nombre de usuario ya existe");
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(99)).thenReturn(false);

        Throwable thrown = catchThrowable(() -> userService.deleteUser(99));

        assertThat(thrown)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void testMostrarUsuarios() {
        List<User> users = Arrays.asList(
                User.builder().id(1).username("uno").build(),
                User.builder().id(2).username("dos").build()
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.mostrarUsuarios();

        assertThat(result).hasSize(2).containsAll(users);
    }

    @Test
    void testObtenerPorUsernameFound() {
        User user = User.builder().username("juan").build();
        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(user));

        User result = userService.obtenerPorUsername("juan");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void testObtenerPorUsernameNotFound() {
        when(userRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> userService.obtenerPorUsername("desconocido"));

        assertThat(thrown)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("no encontrado");
    }
}
