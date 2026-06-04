package com.example.GestorMarcaYModelo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.model.Modelo;
import com.example.GestorMarcaYModelo.repository.MarcaRepository;
import com.example.GestorMarcaYModelo.repository.ModeloRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
@Service
@Transactional
public class ModeloService {
    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    public ModeloService(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
        this.modeloRepository = modeloRepository;
    }
    // metodo para listar todos los modelos
    public List<Modelo> listarModelos() {
        return modeloRepository.findAll();
    }
    // metodo para obtener un modelo por id
    public Modelo obtenerModeloPorId(Integer id) {
        return modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));
    }
    // metodo para guardar un modelo y asociarlo a una marca existente
    public Modelo guardarModelo(Modelo modelo) {
        if (modelo.getIdMarca() == null) {
            throw new IllegalArgumentException("El idMarca es obligatorio.");
        }

        Marca marca = marcaRepository.findById(modelo.getIdMarca())
            .orElseThrow(() -> new EntityNotFoundException("Marca no encontrada con id: " + modelo.getIdMarca()));

        modelo.setMarca(marca);
        return modeloRepository.save(modelo);
    }

    // eliminar un modelo por id
    public void eliminarModelo(Integer id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado con id: " + id));
        modeloRepository.delete(modelo);
    }


}
