package com.example.GestionUsuarios.config;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.GestionUsuarios.repository.UserRepository;
import com.example.GestionUsuarios.webclient.RoleClient;

class LoadDatabaseTest {

    @Test
    void initDatabase_shouldSaveUsersWhenDatabaseIsEmpty() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleClient roleClient = Mockito.mock(RoleClient.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenAnswer(invocation -> "encoded-" + invocation.getArgument(0));
        when(roleClient.obtenerRolPorNombreSinValidacion("Administrador")).thenReturn(Map.of("idRole", 1));
        when(roleClient.obtenerRolPorNombreSinValidacion("Gestor de Inventario")).thenReturn(Map.of("idRole", "2"));
        when(roleClient.obtenerRolPorNombreSinValidacion("Coordinador Logístico")).thenReturn(Map.of("idRole", 3));
        when(roleClient.obtenerRolPorNombreSinValidacion("Soporte Técnico")).thenReturn(Map.of("idRole", "4"));
        when(roleClient.obtenerRolPorNombreSinValidacion("Cliente")).thenReturn(Map.of("idRole", 5));
        when(roleClient.obtenerRolPorNombreSinValidacion("Finanzas")).thenReturn(Map.of("idRole", "6"));

        LoadDatabase loadDatabase = new LoadDatabase(userRepository, roleClient);
        CommandLineRunner runner = loadDatabase.initDatabase(userRepository, passwordEncoder, roleClient);

        runner.run();

        verify(userRepository).saveAll(any());
        verify(roleClient).obtenerRolPorNombreSinValidacion("Administrador");
        verify(passwordEncoder).encode("admin123");
    }

    @Test
    void initDatabase_shouldSkipWhenDatabaseIsNotEmpty() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleClient roleClient = Mockito.mock(RoleClient.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        when(userRepository.count()).thenReturn(1L);

        LoadDatabase loadDatabase = new LoadDatabase(userRepository, roleClient);
        CommandLineRunner runner = loadDatabase.initDatabase(userRepository, passwordEncoder, roleClient);

        runner.run();

        verify(roleClient, never()).obtenerRolPorNombreSinValidacion(any());
        verify(userRepository, never()).saveAll(any());
    }

    @Test
    void initDatabase_shouldFailWhenRoleIdHasUnsupportedType() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        RoleClient roleClient = Mockito.mock(RoleClient.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);

        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(roleClient.obtenerRolPorNombreSinValidacion("Administrador")).thenReturn(Map.of("idRole", true));

        LoadDatabase loadDatabase = new LoadDatabase(userRepository, roleClient);
        CommandLineRunner runner = loadDatabase.initDatabase(userRepository, passwordEncoder, roleClient);

        assertThatThrownBy(runner::run)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID de rol inválido");
    }
}