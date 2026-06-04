package com.example.GestorInventario.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
import com.example.GestorInventario.repository.EquipoRepository;
import com.example.GestorInventario.repository.EstadoRepository;
import com.example.GestorInventario.webclient.MarcaClient;
import com.example.GestorInventario.webclient.ModeloClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstadoService {
    private final EstadoRepository estadoRepository;
    private final EquipoRepository equipoRepository;
    private final MarcaClient marcaClient;
    private final ModeloClient modeloClient;

    public EstadoService(EstadoRepository estadoRepository, EquipoRepository equipoRepository,
            MarcaClient marcaClient, ModeloClient modeloClient) {
        this.estadoRepository = estadoRepository;
        this.equipoRepository = equipoRepository;
        this.marcaClient = marcaClient;
        this.modeloClient = modeloClient;
    }

    // metodo para buscar equipos por estado
    public List<Equipo> buscarEquiposPorEstado(String nombreEstado) {
        Estado estado = estadoRepository.findByNombreEstado(nombreEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        List<Equipo> equipos = equipoRepository.findByEstado(estado);

        equipos.forEach(equipo -> {
            Map<String, Object> marcaMap = marcaClient.obtenerMarcaPorId(equipo.getIdMarca());
            Map<String, Object> modeloMap = modeloClient.obtenerModeloPorId(equipo.getIdModelo());
            equipo.setMarca((String) marcaMap.get("nombre"));
            equipo.setModelo((String) modeloMap.get("nombre"));
            ;
        });

        return equipos;
    }

    // metodo para mostrar todos los estados
    public List<Estado> mostrarTodosLosEstados() {
        try {
            return estadoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los estados: " + e.getMessage());
        }
    }

    public Estado obtenerEstadoPorId(Integer idEstado) {
        return estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + idEstado));
    }
}
