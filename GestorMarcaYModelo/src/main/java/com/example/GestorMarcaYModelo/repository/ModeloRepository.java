package com.example.GestorMarcaYModelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestorMarcaYModelo.model.Modelo;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    List<Modelo> findByMarcaIdMarca(Integer idMarca);

    
}
