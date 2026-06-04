package com.example.RolesyPermisos.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.RolesyPermisos.model.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    //buscar permisos por id
    public Set<Permiso> findByRolesIdRole(Integer idRole);
    //buscar permisos por nombre
    public Permiso findByNombre(String nombre);

}
