package com.example.RolesyPermisos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.example.RolesyPermisos.model.Permiso;
import com.example.RolesyPermisos.service.AutorizacionService;
import com.example.RolesyPermisos.service.PermisoService;

@WebMvcTest(PermisoController.class)
public class PermisoControllerTest {
    @MockBean
    private PermisoService permisoService;

    @MockBean
    private AutorizacionService autorizacionService;

    @Autowired
    private MockMvc mockMvc;

    private final Integer USER_ID = 1;

    @Test
    void obtenerTodosLosPermisos_returnsOK() throws Exception {
        Permiso p1 = new Permiso("Permiso1");
        p1.setIdPermiso(1);
        Permiso p2 = new Permiso("Permiso2");
        p2.setIdPermiso(2);

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(permisoService.obtenerTodosLosPermisos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/v1/permisos")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idPermiso").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Permiso1"))
            .andExpect(jsonPath("$[1].idPermiso").value(2))
            .andExpect(jsonPath("$[1].nombre").value("Permiso2"));
    }

    @Test
    void obtenerPermisoPorId_returnsOK() throws Exception {
        Permiso permiso = new Permiso("Permiso1");
        permiso.setIdPermiso(1);

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(permisoService.obtenerPermisoPorId(1)).thenReturn(permiso);

        mockMvc.perform(get("/api/v1/permisos/1")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idPermiso").value(1))
            .andExpect(jsonPath("$.nombre").value("Permiso1"));
    }

    @Test
    void modificarPermiso_returnsOK() throws Exception {
        Permiso permisoExistente = new Permiso("PermisoExistente");
        permisoExistente.setIdPermiso(1);

        Permiso permisoActualizado = new Permiso("PermisoActualizado");
        permisoActualizado.setIdPermiso(1);

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(permisoService.obtenerPermisoPorId(1)).thenReturn(permisoExistente);
        when(permisoService.guardarPermiso(any(Permiso.class))).thenReturn(permisoActualizado);

        String jsonBody = "{\"nombre\": \"PermisoActualizado\"}";

        mockMvc.perform(put("/api/v1/permisos/modificar/1")
                .header("X-User-Id", USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idPermiso").value(1))
            .andExpect(jsonPath("$.nombre").value("PermisoActualizado"));
    }

    @Test
    void eliminarPermisoTest() throws Exception {
        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        Mockito.doNothing().when(permisoService).eliminarPermisoPorId(1);

        mockMvc.perform(delete("/api/v1/permisos/eliminar/1")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().string("Permiso eliminado correctamente"));
    }

}
