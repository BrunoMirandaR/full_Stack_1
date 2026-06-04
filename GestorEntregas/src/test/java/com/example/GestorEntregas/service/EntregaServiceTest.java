package com.example.GestorEntregas.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.GestorEntregas.model.Entrega;
import com.example.GestorEntregas.repository.EntregaRepository;
import com.example.GestorEntregas.webclient.EstadoClient;
import com.example.GestorEntregas.webclient.PedidoClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {
    @Mock
    private EntregaRepository entregaRepository;
    @Mock
    private EstadoClient estadoClient;
    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private EntregaService entregaService;

    @Test
    void crearEntrega_ok() {
        Entrega entrega = new Entrega();
        entrega.setIdPedido(10);

        Map<String, Object> pedidoMock = new HashMap<>();
        pedidoMock.put("estado", "Pendiente de entrega");

        Map<String, Object> estadoInicialEntrega = new HashMap<>();
        estadoInicialEntrega.put("nombreEstado", "Pendiente de entrega");

        when(pedidoClient.obtenerPedidoPorId(10)).thenReturn(pedidoMock);

        when(estadoClient.obtenerEstadoPorNombre(anyString(), anyInt()))
                .thenReturn(estadoInicialEntrega);

        when(entregaRepository.save(any(Entrega.class))).thenAnswer(i -> i.getArgument(0));

        Entrega resultado = entregaService.crearEntrega(entrega, 5);

        assertNotNull(resultado);
        assertEquals("Pendiente de entrega", resultado.getEstado());
    }

    @Test
    void obtenerTodasLasEntregas_ok() {
        List<Entrega> lista = Arrays.asList(new Entrega(), new Entrega());

        when(entregaRepository.findAll()).thenReturn(lista);

        List<Entrega> resultado = entregaService.obtenerTodasLasEntregas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void actualizarEstadoEntrega_ok() {
        Entrega entrega = new Entrega();
        entrega.setEstado("Pendiente de entrega");
        entrega.setFechaEntrega(null);

        Map<String, Object> estadoMap = new HashMap<>();
        estadoMap.put("nombreEstado", "Entregado");

        when(entregaRepository.findById(1)).thenReturn(Optional.of(entrega));

        when(estadoClient.obtenerEstadoPorNombre(anyString(), anyInt())).thenReturn(estadoMap);

        when(entregaRepository.save(any(Entrega.class))).thenAnswer(i -> i.getArgument(0));

        Entrega resultado = entregaService.actualizarEstadoEntrega(1, "Entregado", 5);

        assertEquals("Entregado", resultado.getEstado());
        assertNotNull(resultado.getFechaEntrega());
    }

    @Test
    void eliminarEntrega_ok() {
        when(entregaRepository.existsById(1)).thenReturn(true);

        entregaService.eliminarEntrega(1);

        verify(entregaRepository, times(1)).deleteById(1);
    }
}
