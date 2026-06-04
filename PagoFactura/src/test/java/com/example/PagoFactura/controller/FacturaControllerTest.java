package com.example.PagoFactura.controller;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.PagoFactura.Model.Factura;
import com.example.PagoFactura.service.AutorizacionService;
import com.example.PagoFactura.service.FacturaService;
import com.example.PagoFactura.service.PagoService;

@WebMvcTest(FacturaController.class)
public class FacturaControllerTest {
    @MockBean
    private FacturaService facturaService;
    @MockBean
    private PagoService pagoService;
    @MockBean
    private AutorizacionService autorizacionService;
    @InjectMocks
    private FacturaController facturaController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGenerarFacturaDesdePedido_ok() throws Exception {
        Factura facturaMock = new Factura();
        facturaMock.setIdPedido(1);
        facturaMock.setEstado("Pendiente");
        facturaMock.setMontoTotal(100.0);

        when(facturaService.generarFacturaDesdePedido(1)).thenReturn(facturaMock);

        // Crear JSON con idPedido
        String jsonBody = "{\"idPedido\":1}";

        mockMvc.perform(post("/api/v1/facturas/generar") // reemplaza con la ruta real
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedido").value(1))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$.montoTotal").value(100.0));
    }

    @Test
    void testObtenerFacturaPorId_ok() throws Exception {
        Integer idUserConectado = 1;
        Integer idFactura = 123;

        Factura facturaMock = new Factura();
        facturaMock.setIdFactura(idFactura);
        facturaMock.setEstado("Pendiente");
        facturaMock.setMontoTotal(200.0);

        when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
                .thenReturn(ResponseEntity.ok().build());

        when(facturaService.obtenerFacturaPorId(idFactura)).thenReturn(facturaMock);

        mockMvc.perform(get("/api/v1/facturas/{id}", idFactura) // <--- pasar idFactura aquí
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFactura").value(idFactura))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$.montoTotal").value(200.0));
    }

    @Test
    void testListarFacturas_ok() throws Exception {
        Integer idUserConectado = 1;

        Factura factura1 = new Factura();
        factura1.setIdFactura(1);
        factura1.setEstado("Pendiente");
        factura1.setMontoTotal(100.0);

        Factura factura2 = new Factura();
        factura2.setIdFactura(2);
        factura2.setEstado("Pagada");
        factura2.setMontoTotal(200.0);

        List<Factura> facturas = List.of(factura1, factura2);

        when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
                .thenReturn(ResponseEntity.ok().build());

        when(facturaService.listarFacturas()).thenReturn(facturas);

        mockMvc.perform(get("/api/v1/facturas")
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idFactura").value(1))
                .andExpect(jsonPath("$[1].estado").value("Pagada"));
    }
    @Test
    void testEliminarFactura_ok() throws Exception {
    Integer idUserConectado = 1;
    Integer idFactura = 10;

    when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
        .thenReturn(ResponseEntity.ok().build());

    doNothing().when(facturaService).eliminarFactura(idFactura);

    mockMvc.perform(delete("/api/v1/facturas/eliminar/{id}", idFactura)
            .header("X-User-Id", idUserConectado))
        .andExpect(status().isNoContent());
}

}
