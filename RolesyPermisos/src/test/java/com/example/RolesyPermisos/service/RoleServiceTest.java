package com.example.RolesyPermisos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.RolesyPermisos.model.Role;
import com.example.RolesyPermisos.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    @Test
    void obtenerTodosLosRolesTest() {
        Role role1 = new Role();
        role1.setIdRole(1);
        role1.setNombre("Admin");
        Role role2 = new Role();
        role2.setIdRole(2);
        role2.setNombre("User");
        List<Role> roles = List.of(role1, role2);

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> resultado = roleService.obtenerTodosLosRoles();

        assertEquals(2, resultado.size());
        verify(roleRepository).findAll();

    }

    @Test
    void obtenerRolPorIdTest() {
        Role role = new Role();
        role.setIdRole(1);
        role.setNombre("Admin");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        Role resultado = roleService.obtenerRolPorId(1);

        assertEquals("Admin", resultado.getNombre());
        verify(roleRepository).findById(1);
    }

    @Test
    void guardarRolTest() {
        Role role = new Role();
        role.setNombre("NuevoRol");

        Role guardado = new Role();
        guardado.setIdRole(1);
        guardado.setNombre("NuevoRol");

        when(roleRepository.save(role)).thenReturn(guardado);

        Role resultado = roleService.guardarRol(role);

        assertEquals(1, resultado.getIdRole());
        assertEquals("NuevoRol", resultado.getNombre());
        verify(roleRepository).save(role);
    }

    @Test
    void actualizarRolTest() {
        Role existente = new Role();
        existente.setIdRole(1);
        existente.setNombre("RolAntiguo");

        Role actualizado = new Role();
        actualizado.setIdRole(1);
        actualizado.setNombre("RolNuevo");

        when(roleRepository.findById(1)).thenReturn(Optional.of(existente));
        when(roleRepository.save(existente)).thenReturn(actualizado);

        Role resultado = roleService.actualizarRol(1, actualizado);

        assertEquals("RolNuevo", resultado.getNombre());
        verify(roleRepository).findById(1);
        verify(roleRepository).save(existente);
    }

    @Test
    void eliminarRolTest() {
        when(roleRepository.existsById(1)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1);

        roleService.eliminarRol(1);

        verify(roleRepository).existsById(1);
        verify(roleRepository).deleteById(1);
    }

    @Test
    void obtenerRolPorNombreTest() {
        Role role = new Role();
        role.setIdRole(1);
        role.setNombre("Admin");

        when(roleRepository.findByNombre("Admin")).thenReturn(Optional.of(role));

        Role resultado = roleService.obtenerRolPorNombre("Admin");

        assertEquals("Admin", resultado.getNombre());
        verify(roleRepository).findByNombre("Admin");
    }

}
