package com.example.GestorPedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestorPedidos.model.Tipo;
@Repository
public interface TipoRepository extends JpaRepository<Tipo, Integer> {
   
    List<Tipo> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}
