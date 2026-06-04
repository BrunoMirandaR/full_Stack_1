package com.example.Autentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Autentication.model.UsuarioConectado;

public interface UsuarioConectadoRepository extends JpaRepository<UsuarioConectado, Integer> {
}
