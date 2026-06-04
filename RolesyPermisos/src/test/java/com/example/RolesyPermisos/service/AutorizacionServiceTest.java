package com.example.RolesyPermisos.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.RolesyPermisos.webclient.UsuarioClient;
import com.example.RolesyPermisos.webclient.UsuarioConectadoClient;

@ExtendWith(MockitoExtension.class)
public class AutorizacionServiceTest {
    @Mock
    private UsuarioConectadoClient usuarioConectadoClient;
    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AutorizacionService autorizacionService;

    @Test
    void obtenerRolUsuarioConectado_devuelveRolCorrectamente() {
        // simulamos que el usuario conectado con ID 3 tiene un userId de 100
        Map<String, Object> conectadoMap = new HashMap<>();
        conectadoMap.put("userId", 100);
        when(usuarioConectadoClient.buscarUsuarioConectadoPorId(3))
                .thenReturn(Optional.of(conectadoMap));

        // simular que el usuario con ID 100 tiene rol 3
        Map<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("idRol", 3);
        when(usuarioClient.obtenerUsuarioPorId(100))
                .thenReturn(Optional.of(usuarioMap));

        Integer rol = autorizacionService.obtenerRolUsuarioConectado(3);
        assertThat(rol).isEqualTo(3);
    }
    @Test
    void validarRol_devuelveOK() {
        // mockear obtenerRolUsuarioConectado para devolver rol esperado 3
        AutorizacionService spyService = spy(autorizacionService);
        doReturn(3).when(spyService).obtenerRolUsuarioConectado(3);

        ResponseEntity<?> response = spyService.validarRol(3, 3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
    @Test
    void validarRoles_devuelveOK_siRolEsperado() {
    // Datos de prueba
    Integer idUserConectado = 5;
    Integer rolDelUsuario = 2;
    Set<Integer> rolesEsperados = Set.of(1, 2, 3);

    // Usamos un spy del service para mockear el método interno
    AutorizacionService spyService = spy(new AutorizacionService(usuarioConectadoClient, usuarioClient));
    doReturn(rolDelUsuario).when(spyService).obtenerRolUsuarioConectado(idUserConectado);

    // Ejecutar
    ResponseEntity<?> response = spyService.validarRoles(idUserConectado, rolesEsperados);

    // Verificar
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNull();
}
}
