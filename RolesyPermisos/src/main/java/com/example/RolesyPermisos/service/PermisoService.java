package com.example.RolesyPermisos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.RolesyPermisos.model.Permiso;
import com.example.RolesyPermisos.repository.PermisoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermisoService {
    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    //metodo para mostrar todos los permisos
    public List<Permiso> obtenerTodosLosPermisos() {
        return permisoRepository.findAll();
    }

    // metodo para obtener un permiso por id
    public Permiso obtenerPermisoPorId(Integer id) {
        return permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    // metodo para guardar un permiso
    public Permiso guardarPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    // metodo para eliminar un permiso por id
    public void eliminarPermisoPorId(Integer id) {
        if (!permisoRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        permisoRepository.deleteById(id);
    }

    // metodo para crear un permiso
    public Permiso crearPermiso(String nombre) {
        Permiso permiso = new Permiso();
        permiso.setNombre(nombre);
        return permisoRepository.save(permiso);
    }

    // metodo para actualizar un permiso
    public Permiso actualizarPermiso(Integer id, String nombre) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permiso.setNombre(nombre);
        return permisoRepository.save(permiso);
    }

}
