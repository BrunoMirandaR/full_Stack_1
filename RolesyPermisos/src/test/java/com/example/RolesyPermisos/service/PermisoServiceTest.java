package com.example.RolesyPermisos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.RolesyPermisos.model.Permiso;
import com.example.RolesyPermisos.repository.PermisoRepository;

@ExtendWith(MockitoExtension.class)
public class PermisoServiceTest {
    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoService permisoService;

    @Test
    void obtenerTodosLosPermisosTest() {
        List<Permiso> permisos = List.of(new Permiso("Permiso1"), new Permiso( "Permiso2"));
        when(permisoRepository.findAll()).thenReturn(permisos);

        List<Permiso> resultado = permisoService.obtenerTodosLosPermisos();

        assertEquals(2, resultado.size());
        verify(permisoRepository).findAll();
    }

    @Test
    void obtenerPermisoPorIdTest() {
        Permiso p = new Permiso("Permiso1");
        when(permisoRepository.findById(1)).thenReturn(Optional.of(p));

        Permiso resultado = permisoService.obtenerPermisoPorId(1);

        assertEquals("Permiso1", resultado.getNombre());
        verify(permisoRepository).findById(1);
    }

    @Test
    void guardarPermisoTest() {
        Permiso p = new Permiso( "NuevoPermiso");
        Permiso guardado = new Permiso( "NuevoPermiso");
        when(permisoRepository.save(p)).thenReturn(guardado);

        Permiso resultado = permisoService.guardarPermiso(p);

        assertEquals("NuevoPermiso", resultado.getNombre());
        verify(permisoRepository).save(p);
    }

    @Test
    void eliminarPermisoPorIdTest() {
        when(permisoRepository.existsById(1)).thenReturn(true);
        doNothing().when(permisoRepository).deleteById(1);

        permisoService.eliminarPermisoPorId(1);

        verify(permisoRepository).existsById(1);
        verify(permisoRepository).deleteById(1);
    }

    @Test
    void crearPermisoTest() {
        Permiso nuevo = new Permiso( "NuevoPermiso");
        Permiso creado = new Permiso( "NuevoPermiso");
        when(permisoRepository.save(any(Permiso.class))).thenReturn(creado);

        Permiso resultado = permisoService.crearPermiso("NuevoPermiso");

        assertEquals("NuevoPermiso", resultado.getNombre());
        verify(permisoRepository).save(any(Permiso.class));
    }

    @Test
    void actualizarPermisoTest() {
        Permiso existente = new Permiso("AntiguoNombre");
        Permiso actualizado = new Permiso( "NuevoNombre");

        when(permisoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(permisoRepository.save(any(Permiso.class))).thenReturn(actualizado);

        Permiso resultado = permisoService.actualizarPermiso(1,"NuevoNombre");

        assertEquals("NuevoNombre", resultado.getNombre());
        verify(permisoRepository).findById(1);
        verify(permisoRepository).save(existente);
    }
}
