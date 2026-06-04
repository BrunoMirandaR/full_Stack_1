package com.example.GestorPedidos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.GestorPedidos.model.Pedido;
import com.example.GestorPedidos.service.AutorizacionService;
import com.example.GestorPedidos.service.PedidoService;
import com.example.GestorPedidos.service.TipoService;
import com.example.GestorPedidos.webclient.UsuarioClient;
import com.example.GestorPedidos.webclient.UsuarioConectadoClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {
    @MockBean
    private PedidoService pedidoService;
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
    void testCrearPedido_exitoso() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("idEquipo", 10);
    body.put("total", 150000.0);
    body.put("idTipo", 2);
    body.put("ciudad", "Santiago");
    body.put("comuna", "Ñuñoa");
    body.put("calle", "Av. Grecia");
    body.put("numero", "1234");
    body.put("depto", "302");
    body.put("referencia", "Frente a la plaza");

    Map<String, Object> conectado = Map.of("userId", 100);
    Map<String, Object> usuario = Map.of("id", 100);
    Map<String, Object> pedidoRespuesta = Map.of("pedido", new Pedido(), "estado", Map.of("nombreEstado", "Pendiente"));

    when(autorizacionService.validarRoles(3, Set.of(3, 6))).thenReturn(ResponseEntity.ok().build());
    when(usuarioConectadoClient.buscarUsuarioConectadoPorId(3)).thenReturn(Optional.of(conectado));
    when(usuarioClient.obtenerUsuarioPorId(100)).thenReturn(Optional.of(usuario));
    when(pedidoService.crearPedido(any(), eq(2))).thenReturn(pedidoRespuesta);

    mockMvc.perform(post("/api/v1/pedidos/crear")
            .header("X-User-Id", 3)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(body)))
        .andExpect(status().isCreated());
}
    @Test
void testBuscarPedidoPorId_ok() throws Exception {
    Map<String, Object> pedidoCompleto = Map.of("idPedido", 1, "estado", "Pendiente");
    when(pedidoService.obtenerPedidoPorId(1)).thenReturn(pedidoCompleto);

    mockMvc.perform(get("/api/v1/pedidos/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.estado").value("Pendiente"));
}
    @Test
void testMostrarTodosLosPedidos_ok() throws Exception {
    List<Pedido> pedidos = List.of(new Pedido(), new Pedido());
    when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
    when(pedidoService.mostrarTodosLosPedidos()).thenReturn(pedidos);

    mockMvc.perform(get("/api/v1/pedidos")
            .header("X-User-Id", 3))
        .andExpect(status().isOk());
}
@Test
void testEliminarPedidoPorId_ok() throws Exception {
    when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
    doNothing().when(pedidoService).eliminarPedidoPorId(1);

    mockMvc.perform(delete("/api/v1/pedidos/eliminar/1")
            .header("X-User-Id", 3))
        .andExpect(status().isNoContent());
}
@Test
void testModificarPedido_ok() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("idEquipo", 5);
    body.put("total", 180000.0);
    body.put("idTipo", 2);
    body.put("idEstado", 1);

    Map<String, Object> conectado = Map.of("userId", 100);
    Pedido pedidoModificado = new Pedido();
    pedidoModificado.setIdPedido(1);

    when(autorizacionService.validarRol(3, 3)).thenReturn(ResponseEntity.ok().build());
    when(usuarioConectadoClient.buscarUsuarioConectadoPorId(3)).thenReturn(Optional.of(conectado));
    when(pedidoService.modificarPedido(eq(1), any(), eq(2))).thenReturn(pedidoModificado);

    mockMvc.perform(put("/api/v1/pedidos/modificar/1")
            .header("X-User-Id", 3)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(body)))
        .andExpect(status().isOk());
}
}
