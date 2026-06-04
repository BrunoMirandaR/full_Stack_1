package com.example.SoporteTecnico.controller;

import com.example.SoporteTecnico.model.TipoSoporte;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TipoSoporteController.class)
public class TipoSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteTecnicoService sopService;

    @MockBean
    private AutorizacionService autorizacionService;

    private final Integer idUserConectado = 123;
    @Test
    void crearTipoSoporteTest() throws Exception {
        TipoSoporte tipoSoporte = new TipoSoporte();
        tipoSoporte.setId(1);
        tipoSoporte.setNombre("Soporte Nuevo");

        when(autorizacionService.validarRol(idUserConectado, 4))
            .thenReturn(ResponseEntity.ok().build());

        when(sopService.createTipoSoporte(any(TipoSoporte.class))).thenReturn(tipoSoporte);

        String json = "{\"nombre\": \"Soporte Nuevo\"}";

        mockMvc.perform(post("/api/v1/tiposSoporte/crear")
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Soporte Nuevo"));
    }

    @Test
    void editarTipoSoporteTest() throws Exception {
        TipoSoporte actualizado = new TipoSoporte();
        actualizado.setId(1);
        actualizado.setNombre("Soporte Editado");

        when(autorizacionService.validarRol(idUserConectado, 4))
            .thenReturn(ResponseEntity.ok().build());

        when(sopService.updateTipoSoporte(eq(1), any(TipoSoporte.class))).thenReturn(actualizado);

        String json = "{\"nombre\": \"Soporte Editado\"}";

        mockMvc.perform(put("/api/v1/tiposSoporte/modificar/1")
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Soporte Editado"));
    }

    @Test
    void listarTiposSoporteTest() throws Exception {
        TipoSoporte ts1 = new TipoSoporte();
        ts1.setId(1);
        ts1.setNombre("Tipo 1");
        TipoSoporte ts2 = new TipoSoporte();
        ts2.setId(2);
        ts2.setNombre("Tipo 2");

        List<TipoSoporte> lista = List.of(ts1, ts2);

        when(autorizacionService.validarRol(idUserConectado, 4))
            .thenReturn(ResponseEntity.ok().build());

        when(sopService.getTipoSoportes()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/tiposSoporte")
                .header("X-User-Id", idUserConectado))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Tipo 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].nombre").value("Tipo 2"));
    }

    @Test
    void eliminarTipoSoporteTest() throws Exception {
        when(autorizacionService.validarRol(idUserConectado, 4))
            .thenReturn(ResponseEntity.ok().build());

        when(sopService.deleteTipoSoporteById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tiposSoporte/eliminar/1")
                .header("X-User-Id", idUserConectado))
            .andExpect(status().isOk())
            .andExpect(content().string("TipoSoporte eliminado correctamente."));
    }
}
