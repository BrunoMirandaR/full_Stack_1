package com.example.PagoFactura.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.PagoFactura.Model.Factura;
import com.example.PagoFactura.WebClient.PedidoClient;
import com.example.PagoFactura.repository.FacturaRepository;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTest {
    @Mock
    private FacturaRepository  facturaRepository;

    @Mock
    private PedidoClient pedidoClient; 

    @InjectMocks
    private FacturaService facturaService;

    @Test
    void testGenerarFacturaDesdePedido_ok() {
        Integer idPedido = 1;
        Map<String, Object> pedidoData = new HashMap<>();
        pedidoData.put("total", 1500.0);

        when(pedidoClient.obtenerPedidoPorId(idPedido)).thenReturn(Optional.of(pedidoData));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Factura factura = facturaService.generarFacturaDesdePedido(idPedido);

        assertThat(factura.getIdPedido()).isEqualTo(idPedido);
        assertThat(factura.getMontoTotal()).isEqualTo(1500.0);
        assertThat(factura.getEstado()).isEqualTo("Pendiente");
        assertThat(factura.getFechaEmision()).isEqualTo(LocalDate.now());
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void testObtenerFacturaPorId_ok() {
        Integer idFactura = 1;
        Factura facturaMock = new Factura();
        facturaMock.setIdFactura(idFactura);

        when(facturaRepository.findById(idFactura)).thenReturn(Optional.of(facturaMock));

        Factura factura = facturaService.obtenerFacturaPorId(idFactura);

        assertThat(factura).isEqualTo(facturaMock);
        verify(facturaRepository).findById(idFactura);
    }

    @Test
    void testListarFacturas_ok() {
        List<Factura> facturas = List.of(new Factura(), new Factura());
        when(facturaRepository.findAll()).thenReturn(facturas);

        List<Factura> resultado = facturaService.listarFacturas();

        assertThat(resultado).isEqualTo(facturas);
        verify(facturaRepository).findAll();
    }

    @Test
    void testMarcarComoPagada_ok() {
        Integer idFactura = 1;
        Factura facturaMock = new Factura();
        facturaMock.setIdFactura(idFactura);
        facturaMock.setEstado("Pendiente");

        when(facturaRepository.findById(idFactura)).thenReturn(Optional.of(facturaMock));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Factura facturaActualizada = facturaService.marcarComoPagada(idFactura);

        assertThat(facturaActualizada.getEstado()).isEqualTo("Pagada");
        verify(facturaRepository).save(facturaMock);
    }

    @Test
    void testEliminarFactura_ok() {
        Integer idFactura = 1;
        when(facturaRepository.existsById(idFactura)).thenReturn(true);

        facturaService.eliminarFactura(idFactura);

        verify(facturaRepository).deleteById(idFactura);
    }

    @Test
    void testGenerarFacturaDesdePedido_whenPedidoMissing_shouldThrow() {
        when(pedidoClient.obtenerPedidoPorId(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.generarFacturaDesdePedido(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Pedido no encontrado");
    }

    @Test
    void testObtenerFacturaPorId_whenMissing_shouldThrow() {
        when(facturaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.obtenerFacturaPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Factura no encontrada");
    }

    @Test
    void testMarcarComoPagada_whenMissing_shouldThrow() {
        when(facturaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.marcarComoPagada(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Factura no encontrada");
    }

    @Test
    void testEliminarFactura_whenMissing_shouldThrow() {
        when(facturaRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> facturaService.eliminarFactura(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Factura no encontrada");
    }
}
