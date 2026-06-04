package com.example.GestorPedidos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.GestorPedidos.model.Tipo;
import com.example.GestorPedidos.service.AutorizacionService;
import com.example.GestorPedidos.service.TipoService;
import com.example.GestorPedidos.webclient.UsuarioClient;
import com.example.GestorPedidos.webclient.UsuarioConectadoClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TipoController.class)
public class TipoControllerTest {
    @MockBean
    private TipoService tipoService;
    @MockBean
    private AutorizacionService autorizacionService;
    @MockBean
    private UsuarioConectadoClient usuarioConectadoClient;
    @MockBean
    private UsuarioClient usuarioClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMostrarTodos_ok() throws Exception {
        Tipo tipo1 = new Tipo();
        tipo1.setIdTipo(1);
        tipo1.setNombre("Arriendo");

        Tipo tipo2 = new Tipo();
        tipo2.setIdTipo(2);
        tipo2.setNombre("Compra");

        List<Tipo> tipos = List.of(tipo1, tipo2);

        when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
        when(tipoService.mostrarTodos()).thenReturn(tipos);

        mockMvc.perform(get("/api/v1/tipos")
                .header("X-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTipo").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Arriendo"))
                .andExpect(jsonPath("$[1].idTipo").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Compra"));
    }

    @Test
    void testCrearTipo_ok() throws Exception {
        Tipo tipoEntrada = new Tipo();
        tipoEntrada.setNombre("Venta");

        Tipo tipoGuardado = new Tipo();
        tipoGuardado.setIdTipo(5);
        tipoGuardado.setNombre("Venta");

        when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
        when(tipoService.crearTipo(any(Tipo.class))).thenReturn(tipoGuardado);

        String json = new ObjectMapper().writeValueAsString(tipoEntrada);

        mockMvc.perform(post("/api/v1/tipos")
                .header("X-User-Id", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTipo").value(5))
                .andExpect(jsonPath("$.nombre").value("Venta"));
    }

    @Test
    void testObtenerPorId_ok() throws Exception {
        Tipo tipo = new Tipo();
        tipo.setIdTipo(3);
        tipo.setNombre("Renta");

        when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
        when(tipoService.obtenerPorId(3)).thenReturn(tipo);

        mockMvc.perform(get("/api/v1/tipos/3")
                .header("X-User-Id", 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipo").value(3))
                .andExpect(jsonPath("$.nombre").value("Renta"));
    }

    @Test
    void testActualizarTipo_ok() throws Exception {
        Tipo tipoEntrada = new Tipo();
        tipoEntrada.setNombre("Actualizado");

        Tipo tipoActualizado = new Tipo();
        tipoActualizado.setIdTipo(4);
        tipoActualizado.setNombre("Actualizado");

        when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
        when(tipoService.actualizarTipo(eq(4), any(Tipo.class))).thenReturn(tipoActualizado);

        String json = new ObjectMapper().writeValueAsString(tipoEntrada);

        mockMvc.perform(put("/api/v1/tipos/modificar/4")
                .header("X-User-Id", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTipo").value(4))
                .andExpect(jsonPath("$.nombre").value("Actualizado"));
    }

    @Test
    void testEliminarTipo_ok() throws Exception {
        when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
        doNothing().when(tipoService).eliminarTipo(7);

        mockMvc.perform(delete("/api/v1/tipos/7")
                .header("X-User-Id", 3))
                .andExpect(status().isNoContent());
    }

}