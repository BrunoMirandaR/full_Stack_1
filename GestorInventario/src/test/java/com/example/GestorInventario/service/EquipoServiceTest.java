package com.example.GestorInventario.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
import com.example.GestorInventario.repository.EquipoRepository;
import com.example.GestorInventario.repository.EstadoRepository;
import com.example.GestorInventario.webclient.MarcaClient;
import com.example.GestorInventario.webclient.ModeloClient;

@ExtendWith(MockitoExtension.class)
public class EquipoServiceTest {
    @Mock
    private EquipoRepository equipoRepository;
    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private MarcaClient marcaClient;
    @Mock
    private ModeloClient modeloClient;

    @InjectMocks
    private EquipoService equipoService;

    @Test
    void testMostrarTodosLosEquipos() {
        // crear un equipo simulado (solo IDs)
        Equipo equipoMock = new Equipo();
        equipoMock.setIdEquipo(1);
        equipoMock.setIdMarca(10);
        equipoMock.setIdModelo(20);

        // marca mock como Map
        Map<String, Object> marcaMock = new HashMap<>();
        marcaMock.put("id", 10);
        marcaMock.put("nombre", "Marca Test");

        // modelo mock como Map
        Map<String, Object> modeloMock = new HashMap<>();
        modeloMock.put("id", 20);
        modeloMock.put("nombre", "Modelo Test");
        // simular el comportamiento del repositorio, marca y modelo
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipoMock));
        when(marcaClient.obtenerMarcaPorId(10)).thenReturn(marcaMock);
        when(modeloClient.obtenerModeloPorId(20)).thenReturn(modeloMock);
        // ejecutar el método a probar
        List<Equipo> resultado = equipoService.mostrarTodosLosEquipos();

        // verificaciones
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getMarca()).isEqualTo("Marca Test");
        assertThat(resultado.get(0).getModelo()).isEqualTo("Modelo Test");
    }

    @Test
    void testObtenerEquipoPorId() {
        Equipo equipoMock = new Equipo();
        equipoMock.setIdEquipo(1);
        equipoMock.setIdMarca(10);
        equipoMock.setIdModelo(20);

        Map<String, Object> marcaMock = new HashMap<>();
        marcaMock.put("idMarca", 10);
        marcaMock.put("nombre", "Marca Test");

        Map<String, Object> modeloMock = new HashMap<>();
        modeloMock.put("idModelo", 20);
        modeloMock.put("nombre", "Modelo Test");

        when(equipoRepository.findById(1)).thenReturn(Optional.of(equipoMock));
        when(marcaClient.obtenerMarcaPorId(10)).thenReturn(marcaMock);
        when(modeloClient.obtenerModeloPorId(20)).thenReturn(modeloMock);

        Equipo resultado = equipoService.obtenerEquipoPorId(1);

        assertThat(resultado.getMarca()).isEqualTo("Marca Test");
        assertThat(resultado.getModelo()).isEqualTo("Modelo Test");
    }

    @Test
    void testGuardarEquipo() {
        Equipo equipoEntrada = new Equipo();
        equipoEntrada.setIdMarca(10);
        equipoEntrada.setIdModelo(20);
        equipoEntrada.setEstado(new Estado(1, "Disponible")); 

        Map<String, Object> marcaMock = new HashMap<>();
        marcaMock.put("idMarca", 10);
        marcaMock.put("nombre", "Marca Test");

        Map<String, Object> modeloMock = new HashMap<>();
        modeloMock.put("idModelo", 20);
        modeloMock.put("nombre", "Modelo Test");

        Estado estadoMock = new Estado(1, "Disponible");

        when(marcaClient.obtenerMarcaPorId(10)).thenReturn(marcaMock);
        when(modeloClient.obtenerModeloPorId(20)).thenReturn(modeloMock);
        when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoMock));
        when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipo resultado = equipoService.guardarEquipo(equipoEntrada);

        assertThat(resultado.getMarca()).isEqualTo("Marca Test");
        assertThat(resultado.getModelo()).isEqualTo("Modelo Test");
        assertThat(resultado.getEstado().getNombreEstado()).isEqualTo("Disponible");
    }

    @Test
    void testEliminarEquipo() {
        when(equipoRepository.existsById(1)).thenReturn(true);

        equipoService.eliminarEquipo(1);

        verify(equipoRepository).deleteById(1);
    }
}
