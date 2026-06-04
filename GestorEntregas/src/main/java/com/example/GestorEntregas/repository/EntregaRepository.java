package com.example.GestorEntregas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestorEntregas.model.Entrega;
@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
   // metodo para buscar entregas por estado
    List<Entrega> findByEstado(String estado);

}
