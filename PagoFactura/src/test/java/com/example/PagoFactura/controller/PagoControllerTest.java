package com.example.PagoFactura.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.example.PagoFactura.Model.Pago;
import com.example.PagoFactura.service.AutorizacionService;
import com.example.PagoFactura.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {
    @MockBean
    private PagoService pagoService;
    @MockBean
    private AutorizacionService autorizacionService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegistrarPago_ok() throws Exception {
        Integer idUserConectado = 1;

        Pago pago = new Pago();
        pago.setIdFactura(5);
        pago.setMontoPagado(100.0);
        // ... otros campos si los hay

        Pago pagoGuardado = new Pago();
        pagoGuardado.setIdPago(10);
        pagoGuardado.setIdFactura(5);
        pagoGuardado.setMontoPagado(100.0);
        pagoGuardado.setEstado("Completado");
        pagoGuardado.setFechaPago(LocalDate.now());

        when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
                .thenReturn(ResponseEntity.ok().build());

        when(pagoService.registrarPago(any(Pago.class))).thenReturn(pagoGuardado);

        String pagoJson = new ObjectMapper().writeValueAsString(pago);

        mockMvc.perform(post("/api/v1/pagos") // ajusta la ruta según tu controller
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pagoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPago").value(10))
                .andExpect(jsonPath("$.idFactura").value(5))
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    @Test
    void testListarPagosPorFactura_ok() throws Exception {
        Integer idUserConectado = 1;
        Integer idFactura = 5;

        Pago pago1 = new Pago();
        pago1.setIdPago(100);
        pago1.setIdFactura(idFactura);
        pago1.setMontoPagado(200.0);

        Pago pago2 = new Pago();
        pago2.setIdPago(101);
        pago2.setIdFactura(idFactura);
        pago2.setMontoPagado(150.0);

        List<Pago> listaPagos = List.of(pago1, pago2);

        // Simular autorización OK
        when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
                .thenReturn(ResponseEntity.ok().build());

        // Simular devolución de lista de pagos
        when(pagoService.listarPagosPorFactura(idFactura)).thenReturn(listaPagos);

        mockMvc.perform(get("/api/v1/pagos/factura/{idFactura}", idFactura)
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPago").value(100))
                .andExpect(jsonPath("$[1].idPago").value(101))
                .andExpect(jsonPath("$[0].idFactura").value(idFactura));
    }

    @Test
    void testEliminarPago_ok() throws Exception {
        Integer idUserConectado = 1;
        Integer idPago = 10;

        // Simular autorización exitosa
        when(autorizacionService.validarRoles(eq(idUserConectado), anySet()))
                .thenReturn(ResponseEntity.ok().build());

        // Simular que pagoService.eliminarPago no lanza excepción (void)
        doNothing().when(pagoService).eliminarPago(idPago);

        mockMvc.perform(delete("/api/v1/pagos/eliminar/{id}", idPago)
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isNoContent());
    }
}
