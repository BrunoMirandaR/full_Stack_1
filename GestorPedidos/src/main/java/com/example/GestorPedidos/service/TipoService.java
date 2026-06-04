package com.example.GestorPedidos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestorPedidos.model.Tipo;
import com.example.GestorPedidos.repository.TipoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TipoService {
    private final TipoRepository tipoRepository;
 
    public TipoService(TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }

    // obtener un tipo por ID
    public Tipo obtenerPorId(Integer id) {
        return tipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo con ID " + id + " no encontrado."));
    }

    // mostrar todos los tipos
    public List<Tipo> mostrarTodos() {
        try {
            return tipoRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los tipos: " + e.getMessage());
        }
    }

    // crear un nuevo tipo
    public Tipo crearTipo(Tipo tipo) {
        if (tipo.getIdTipo() != null) {
            throw new RuntimeException("El id debe ser nulo al crear un nuevo tipo");
        }
        return tipoRepository.save(tipo);
    }

    // actualizar un tipo existente
    public Tipo actualizarTipo(Integer id, Tipo tipoActualizado) {
        Tipo tipoExistente = obtenerPorId(id);

        tipoExistente.setNombre(tipoActualizado.getNombre());

        return tipoRepository.save(tipoExistente);
    }

    // eliminar un tipo por ID
    public void eliminarTipo(Integer id) {
        if (!tipoRepository.existsById(id)) {
            throw new RuntimeException("Tipo con ID " + id + " no encontrado para eliminar.");
        }
        tipoRepository.deleteById(id);
    }
}
