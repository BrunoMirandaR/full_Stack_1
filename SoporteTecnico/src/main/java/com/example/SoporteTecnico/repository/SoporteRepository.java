package com.example.SoporteTecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SoporteTecnico.model.Soporte;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Integer> {

}
