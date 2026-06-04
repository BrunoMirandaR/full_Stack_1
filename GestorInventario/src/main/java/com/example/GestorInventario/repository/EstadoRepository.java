package com.example.GestorInventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestorInventario.model.Estado;
@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
   
    Optional<Estado> findByNombreEstado(String nombreEstado);

}
