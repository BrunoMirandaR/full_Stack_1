package com.example.RolesyPermisos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RolesyPermisos.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
   // Buscar roles por id
    Optional <Role> findByNombre(String nombre);
    
}
