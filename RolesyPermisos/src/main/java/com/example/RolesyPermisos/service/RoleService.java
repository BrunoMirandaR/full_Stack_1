package com.example.RolesyPermisos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RolesyPermisos.model.Role;
import com.example.RolesyPermisos.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class RoleService {
    @Autowired
    private  RoleRepository roleRepository;
    

    public List<Role> obtenerTodosLosRoles() {
        List<Role> roles = roleRepository.findAll();

        // Forzar carga de permisos para evitar Lazy Loading
        roles.forEach(role -> Hibernate.initialize(role.getPermisos()));

        return roles;
    }

    public Role obtenerRolPorId(Integer id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public Role guardarRol(Role role) {
        return roleRepository.save(role);
    }

    public Role actualizarRol(Integer id, Role roleActualizado) {
        Role rolExistente = obtenerRolPorId(id);
        rolExistente.setNombre(roleActualizado.getNombre());

        return roleRepository.save(rolExistente);
    }

    public void eliminarRol(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el rol con ID: " + id);
        }
        roleRepository.deleteById(id);
    }

    public Role obtenerRolPorNombre(String nombre) {
        return roleRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con nombre: " + nombre));
    }
}
