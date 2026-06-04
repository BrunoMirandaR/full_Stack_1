package com.example.GestorMarcaYModelo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.example.GestorMarcaYModelo.model.Modelo;
import com.example.GestorMarcaYModelo.service.ModeloService;

@WebMvcTest(ModeloController.class)
public class ModeloControllerTest {

    @MockBean
    private ModeloService modeloService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarModelos_returnsOKAndJson() throws Exception {
        List<Modelo> listaModelos = Arrays.asList(new Modelo(1, "Modelo A", null));

        when(modeloService.listarModelos()).thenReturn(listaModelos);

        mockMvc.perform(get("/api/v1/modelos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idModelo").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Modelo A"));
    }

    @Test
    void obtenerModeloPorId_existingId_returnsOKAndJson() throws Exception {
        Modelo modelo = new Modelo(1, "Modelo A", null);

        when(modeloService.obtenerModeloPorId(1)).thenReturn(modelo);

        mockMvc.perform(get("/api/v1/modelos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idModelo").value(1))
                .andExpect(jsonPath("$.nombre").value("Modelo A"));
    }

    @Test
    void crearModelo_returnsCreatedAndJson() throws Exception {
        Modelo nuevoModelo = new Modelo(null, "Modelo Nuevo", null);
        Modelo modeloGuardado = new Modelo(1, "Modelo Nuevo", null);

        when(modeloService.guardarModelo(any(Modelo.class))).thenReturn(modeloGuardado);

        mockMvc.perform(post("/api/v1/modelos/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Modelo Nuevo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idModelo").value(1))
                .andExpect(jsonPath("$.nombre").value("Modelo Nuevo"));
    }

    @Test
    void modificarModelo_existingId_returnsOKAndUpdatedJson() throws Exception {
        Modelo modeloExistente = new Modelo(1, "Modelo Antiguo", null);
        Modelo modeloActualizado = new Modelo(1, "Modelo Actualizado", null);

        when(modeloService.obtenerModeloPorId(1)).thenReturn(modeloExistente);
        when(modeloService.guardarModelo(any(Modelo.class))).thenReturn(modeloActualizado);

        mockMvc.perform(put("/api/v1/modelos/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Modelo Actualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idModelo").value(1))
                .andExpect(jsonPath("$.nombre").value("Modelo Actualizado"));
    }

    @Test
    void eliminarModelo_returnsNoContent() throws Exception {
        doNothing().when(modeloService).eliminarModelo(1);

        mockMvc.perform(delete("/api/v1/modelos/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
