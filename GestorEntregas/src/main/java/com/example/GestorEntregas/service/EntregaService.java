package com.example.GestorEntregas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.GestorEntregas.model.Entrega;
import com.example.GestorEntregas.repository.EntregaRepository;
import com.example.GestorEntregas.webclient.EstadoClient;
import com.example.GestorEntregas.webclient.PedidoClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EntregaService {
    private final EstadoClient estadoClient;
    private final PedidoClient pedidoClient;
    private final EntregaRepository entregaRepository;

    public EntregaService(EstadoClient estadoClient, EntregaRepository entregaRepository,
            PedidoClient pedidoClient) {
        this.estadoClient = estadoClient;
        this.entregaRepository = entregaRepository;
        this.pedidoClient = pedidoClient;
    }

    // metodo para crear una nueva entrega
    public Entrega crearEntrega(Entrega entrega,Integer idUserConectado) {
        if (entrega.getIdEntrega() != null) {
            throw new RuntimeException("No debes enviar el id al crear una nueva entrega");
        }

        Map<String, Object> pedidoData = pedidoClient.obtenerPedidoPorId(entrega.getIdPedido());
        if (pedidoData == null) {
            throw new RuntimeException("Pedido no encontrado");
        }

        // obtener el estado del pedido
        Object estadoObj = pedidoData.get("estado");
        if (!(estadoObj instanceof String)) {
            throw new RuntimeException("El pedido no tiene estado asignado");
        }

        String nombreEstadoPedido = (String) estadoObj;

        if (!"Pendiente de entrega".equalsIgnoreCase(nombreEstadoPedido)) {
            throw new RuntimeException("Pedido no está listo para entrega");
        }

        // Obtener estado inicial para la entrega
        Map<String, Object> estadoMap = estadoClient.obtenerEstadoPorNombre("Pendiente de entrega", idUserConectado);
        if (estadoMap == null || estadoMap.get("nombreEstado") == null) {
            throw new RuntimeException("No se pudo obtener el estado inicial de entrega");
        }

        entrega.setEstado(estadoMap.get("nombreEstado").toString());

        return entregaRepository.save(entrega);
    }

    // metodo para obtener todas las entregas
    public List<Entrega> obtenerTodasLasEntregas() {
        try {
            return entregaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las entregas: " + e.getMessage());
        }
    }

    // metodo para modificar el estado de una entrega
    public Entrega actualizarEstadoEntrega(Integer idEntrega, String nuevoEstado, Integer idUserConectado) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada con id: " + idEntrega));

        // llamamos al WebClient usando Map
        Map<String, Object> estadoMap = estadoClient.obtenerEstadoPorNombre(nuevoEstado, idUserConectado);

        String nombreEstado = estadoMap.get("nombreEstado").toString();

        entrega.setEstado(nombreEstado);

        if (nombreEstado.equalsIgnoreCase("Entregado")) {
            entrega.setFechaEntrega(LocalDate.now());
        }

        return entregaRepository.save(entrega);
    }

    // metodo para eliminar una entrega por id
    public void eliminarEntrega(Integer idProceso) {
        if (!entregaRepository.existsById(idProceso)) {
            throw new RuntimeException("Entrega no encontrada con id: " + idProceso);
        }
        entregaRepository.deleteById(idProceso);
    }

}
