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
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.service.MarcaService;

@WebMvcTest(MarcaController.class)
public class MarcaControllerTest {
    @MockBean
    private MarcaService marcaService;

    @Autowired
    private MockMvc mockMvc;


    //listar todas las marcas
    @Test
    void listarMarcas_returnsOKAndJson() throws Exception {
        List<Marca> marcas = List.of(new Marca(1, "Marca A"));
        when(marcaService.listarMarcas()).thenReturn(marcas);

        mockMvc.perform(get("/api/v1/marcas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].idMarca").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Marca A"));
    }
    
    // obtener marca por ID existente
    @Test
    void obtenerMarcaPorId_existingId_returnsOKAndJson() throws Exception {
        Marca marca = new Marca(1, "Marca A");
        when(marcaService.obtenerMarcaPorId(1)).thenReturn(marca);

        mockMvc.perform(get("/api/v1/marcas/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idMarca").value(1))
            .andExpect(jsonPath("$.nombre").value("Marca A"));
    }
    // crear una nueva marca
    @Test
    void crearMarca_returnsCreatedAndJson() throws Exception {
        Marca nueva = new Marca(null, "Marca Nueva");
        Marca creada = new Marca(1, "Marca Nueva");

        when(marcaService.guardarMarca(any(Marca.class))).thenReturn(creada);

        mockMvc.perform(post("/api/v1/marcas/guardar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Marca Nueva\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.idMarca").value(1))
            .andExpect(jsonPath("$.nombre").value("Marca Nueva"));
    }

    // modificar una marca 
    @Test
    void modificarMarca_existingId_returnsOKAndUpdatedJson() throws Exception {
        Marca existente = new Marca(1, "Marca Antiguo");
        Marca actualizada = new Marca(1, "Marca Actualizada");

        when(marcaService.obtenerMarcaPorId(1)).thenReturn(existente);
        when(marcaService.guardarMarca(any(Marca.class))).thenReturn(actualizada);

        mockMvc.perform(put("/api/v1/marcas/modificar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\": \"Marca Actualizada\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idMarca").value(1))
            .andExpect(jsonPath("$.nombre").value("Marca Actualizada"));
    }
    // eliminar una marca por ID
    @Test
    void eliminarMarca_existingId_returnsNoContent() throws Exception {
        doNothing().when(marcaService).eliminarMarca(1);

        mockMvc.perform(delete("/api/v1/marcas/eliminar/1"))
            .andExpect(status().isNoContent());
    }
}
