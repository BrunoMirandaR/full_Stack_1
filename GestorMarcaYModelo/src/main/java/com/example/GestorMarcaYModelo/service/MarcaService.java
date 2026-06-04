package com.example.GestorMarcaYModelo.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.repository.MarcaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MarcaService {
    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }
    //mostrar todas las marcas 
    public List<Marca> listarMarcas() {
        try {
            return marcaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al mostrar las marcas"+ e.getMessage(),e); 
        }
    }
    //obtener una marca por id
    public Marca obtenerMarcaPorId(Integer idMarca) {
        return marcaRepository.findById(idMarca)
                .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + idMarca));
    }
    //guardar una marca
    public Marca guardarMarca(Marca marca) {
        if (marca.getNombre() == null || marca.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la marca es obligatorio");
        }

        try {
            return marcaRepository.save(marca);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al guardar la marca en la base de datos", e);
        }
    }

    // eliminar una marca por id
    public void eliminarMarca(Integer idMarca) {
        if (idMarca == null) {
            throw new IllegalArgumentException("El ID de la marca no puede ser nulo");
        }
        if (!marcaRepository.existsById(idMarca)) {
            throw new RuntimeException("Marca no encontrada con ID: " + idMarca);
        }
        try {
            marcaRepository.deleteById(idMarca);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error al eliminar la marca de la base de datos", e);
        }
    }
   
}
