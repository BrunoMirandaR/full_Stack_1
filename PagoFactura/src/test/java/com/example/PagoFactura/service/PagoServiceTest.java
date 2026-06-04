package com.example.PagoFactura.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.PagoFactura.Model.Factura;
import com.example.PagoFactura.Model.Pago;
import com.example.PagoFactura.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {
    @Mock
    private FacturaService facturaService;
    @Mock
    private PagoRepository pagoRepository;
    @InjectMocks
    private PagoService pagoService;

    @Test
    void testRegistrarPago_CubreFactura_PagaFactura() {
    Pago pago = new Pago();
    pago.setIdFactura(1);
    pago.setMontoPagado(500.0);

    Factura factura = new Factura();
    factura.setIdFactura(1);
    factura.setMontoTotal(500.0);
    factura.setEstado("Pendiente");

    when(facturaService.obtenerFacturaPorId(1)).thenReturn(factura);
    when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(pagoRepository.sumPagosPorFactura(1)).thenReturn(500.0);
    when(facturaService.actualizarFactura(any(Factura.class))).thenReturn(factura);  // <-- Cambiado aquí

    Pago resultado = pagoService.registrarPago(pago);

    assertThat(resultado.getFechaPago()).isEqualTo(LocalDate.now());
    assertThat(resultado.getEstado()).isEqualTo("Completado");
    verify(facturaService).actualizarFactura(factura);
    assertThat(factura.getEstado()).isEqualTo("Pagada");
}
    @Test
    void testListarPagosPorFactura_DevuelveLista() {
        Integer idFactura = 5;
        List<Pago> pagos = List.of(new Pago(), new Pago());

        when(pagoRepository.findByIdFactura(idFactura)).thenReturn(pagos);

        List<Pago> resultado = pagoService.listarPagosPorFactura(idFactura);

        assertThat(resultado).isEqualTo(pagos);
    }

    @Test
    void testEliminarPago_Existente_Elimina() {
        Integer idPago = 10;
        when(pagoRepository.existsById(idPago)).thenReturn(true);
        doNothing().when(pagoRepository).deleteById(idPago);

        pagoService.eliminarPago(idPago);

        verify(pagoRepository).deleteById(idPago);
    }
}
