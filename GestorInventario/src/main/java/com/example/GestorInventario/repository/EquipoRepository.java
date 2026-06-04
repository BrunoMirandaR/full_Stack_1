package com.example.GestorInventario.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
     //metodo para buscar equipos por estado
    List<Equipo> findByEstado(Estado estado);

    

}
