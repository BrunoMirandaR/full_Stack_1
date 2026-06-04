package com.example.RolesyPermisos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.example.RolesyPermisos.model.Role;
import com.example.RolesyPermisos.service.AutorizacionService;
import com.example.RolesyPermisos.service.RoleService;
@WebMvcTest(RolController.class)
public class RolControllerTest {

    @MockBean
    private RoleService roleService;
    @MockBean
    private AutorizacionService autorizacionService;

    @Autowired
    private MockMvc mockMvc;
    
    private final Integer USER_ID = 1;

    @Test
    void obtenerTodosLosRoles_ok() throws Exception {
        Role r1 = new Role();
        r1.setIdRole(1);
        r1.setNombre("Admin");
        Role r2 = new Role();
        r2.setIdRole(2);
        r2.setNombre("User");

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(roleService.obtenerTodosLosRoles()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v1/roles")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idRole").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Admin"))
            .andExpect(jsonPath("$[1].idRole").value(2))
            .andExpect(jsonPath("$[1].nombre").value("User"));
    }

    @Test
    void obtenerRolPorId_ok() throws Exception {
        Role role = new Role();
        role.setIdRole(1);
        role.setNombre("Admin");

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(roleService.obtenerRolPorId(1)).thenReturn(role);

        mockMvc.perform(get("/api/v1/roles/1")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idRole").value(1))
            .andExpect(jsonPath("$.nombre").value("Admin"));
    }

    @Test
    void crearRol_ok() throws Exception {
        Role creado = new Role();
        creado.setIdRole(1);
        creado.setNombre("NewRole");

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(roleService.guardarRol(any(Role.class))).thenReturn(creado);

        String jsonBody = "{\"nombre\":\"NewRole\"}";

        mockMvc.perform(post("/api/v1/roles/crear")
                .header("X-User-Id", USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idRole").value(1))
            .andExpect(jsonPath("$.nombre").value("NewRole"));
    }

    @Test
    void actualizarRol_ok() throws Exception {
        Role actualizado = new Role();
        actualizado.setIdRole(1);
        actualizado.setNombre("UpdatedRole");

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(roleService.actualizarRol(eq(1), any(Role.class))).thenReturn(actualizado);

        String jsonBody = "{\"nombre\":\"UpdatedRole\"}";

        mockMvc.perform(put("/api/v1/roles/actualizar/1")
                .header("X-User-Id", USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idRole").value(1))
            .andExpect(jsonPath("$.nombre").value("UpdatedRole"));
    }

    @Test
    void eliminarRol_ok() throws Exception {
        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        doNothing().when(roleService).eliminarRol(1);

        mockMvc.perform(delete("/api/v1/roles/eliminar/1")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerRolPorNombre_ok() throws Exception {
        Role role = new Role();
        role.setIdRole(1);
        role.setNombre("Admin");

        when(autorizacionService.validarRol(USER_ID, 1)).thenReturn(ResponseEntity.ok().build());
        when(roleService.obtenerRolPorNombre("Admin")).thenReturn(role);

        mockMvc.perform(get("/api/v1/roles/nombre/Admin")
                .header("X-User-Id", USER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idRole").value(1))
            .andExpect(jsonPath("$.nombre").value("Admin"));
    }
}
