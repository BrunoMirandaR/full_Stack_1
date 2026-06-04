package com.example.SoporteTecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SoporteTecnico.model.TipoSoporte;

@Repository
public interface TipoSoporteRepository extends JpaRepository<TipoSoporte, Integer> {

}
