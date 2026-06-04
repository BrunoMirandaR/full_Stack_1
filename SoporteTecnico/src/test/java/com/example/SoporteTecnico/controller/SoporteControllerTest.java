package com.example.SoporteTecnico.controller;

import com.example.SoporteTecnico.model.Soporte;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(SoporteController.class)
public class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteTecnicoService sopService;

    @MockBean
    private AutorizacionService autorizacionService;

    private final Integer idUserConectado = 123;

    @Test
    void crearSoporteTest() throws Exception {
        Soporte soporte = new Soporte(); // setea campos mínimos necesarios si hay validaciones
        soporte.setId(1);
        soporte.setObservacion("Soporte nuevo");

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());

        when(sopService.createSoporte(any(Soporte.class))).thenReturn(soporte);

        String json = "{\"descripcion\": \"Soporte nuevo\"}";

        mockMvc.perform(post("/api/v1/soportes/crear") // pon la ruta correcta según tu @RequestMapping
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.observacion").value("Soporte nuevo"));
    }

    @Test
    void editarSoporteTest() throws Exception {
        Soporte soporteActualizado = new Soporte();
        soporteActualizado.setId(1);
        soporteActualizado.setObservacion("Soporte editado");

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());

        when(sopService.updateSoporte(eq(1), any(Soporte.class))).thenReturn(soporteActualizado);

        String json = "{\"observacion\": \"Soporte editado\"}";

        mockMvc.perform(put("/api/v1/soportes/modificar/1") // ruta: /{id}
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.observacion").value("Soporte editado"));
    }

    @Test
    void listarSoportes() throws Exception {
        Soporte s1 = new Soporte();
        s1.setId(1);
        s1.setObservacion("Soporte 1");
        Soporte s2 = new Soporte();
        s2.setId(2);
        s2.setObservacion("Soporte 2");

        List<Soporte> lista = List.of(s1, s2);

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());

        when(sopService.getSoportes()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/soportes")
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].observacion").value("Soporte 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].observacion").value("Soporte 2"));
    }

    @Test
    void eliminarSoporte() throws Exception {
        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());

        when(sopService.deleteSoporteById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/soportes/eliminar/1") 
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(content().string("Soporte eliminado correctamente."));
    }
}
