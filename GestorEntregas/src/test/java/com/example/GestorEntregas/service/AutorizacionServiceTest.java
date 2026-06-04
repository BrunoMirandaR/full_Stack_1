package com.example.GestorEntregas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.GestorEntregas.webclient.UsuarioClient;
import com.example.GestorEntregas.webclient.UsuarioConectadoClient;
@ExtendWith(MockitoExtension.class)
public class AutorizacionServiceTest {
    @Mock 
    private UsuarioConectadoClient usuarioConectadoClient;
    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AutorizacionService autorizacionService;


    @Test
    void obtenerRolUsuarioConectadoOk() {
        Integer userIdConectado = 1;
        // Simulamos que el usuario conectado con ID 1 tiene un userId de 100
        Map<String, Object> userConectado = new HashMap<>();
        userConectado.put("userId", 100);
        when(usuarioConectadoClient.buscarUsuarioConectadoPorId(userIdConectado))
                .thenReturn(Optional.of(userConectado));

        // Simulamos que el usuario con ID 100 tiene rol 5
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("idRol", 5);
        when(usuarioClient.obtenerUsuarioPorId(100))
                .thenReturn(Optional.of(usuario));

        Integer rol = autorizacionService.obtenerRolUsuarioConectado(userIdConectado);
        assertEquals(5, rol);
    }

    @Test
    void validarRolOk() {
        Integer userIdConectado = 5;
        Integer rolEsperado = 9;

        // mock para obtener rol igual al esperado
        AutorizacionService spyService = spy(autorizacionService);
        doReturn(rolEsperado).when(spyService).obtenerRolUsuarioConectado(userIdConectado);

        ResponseEntity<?> response = spyService.validarRol(userIdConectado, rolEsperado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
