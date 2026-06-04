package com.example.GestorInventario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
import com.example.GestorInventario.repository.EquipoRepository;
import com.example.GestorInventario.repository.EstadoRepository;
import com.example.GestorInventario.webclient.MarcaClient;
import com.example.GestorInventario.webclient.ModeloClient;

@ExtendWith(MockitoExtension.class)
public class EstadoServiceTest {
    @Mock
    private EquipoRepository equipoRepository;
    @Mock
    private EstadoRepository estadoRepository;
    @Mock
    private MarcaClient marcaClient;
    @Mock
    private ModeloClient modeloClient;

    @InjectMocks
    private EstadoService estadoService;

    @Test
    void testBuscarEquiposPorEstado() {
        String nombreEstado = "Disponible";

    // Mock del estado
    Estado estadoMock = new Estado();
    estadoMock.setIdEstado(1);
    estadoMock.setNombreEstado(nombreEstado);

    // Equipo mock
    Equipo equipoMock = new Equipo();
    equipoMock.setIdEquipo(1);
    equipoMock.setIdMarca(10);
    equipoMock.setIdModelo(20);
    equipoMock.setEstado(estadoMock);

    // Mock de marca y modelo
    Map<String, Object> marcaMock = Map.of("idMarca", 10, "nombre", "John Deere");
    Map<String, Object> modeloMock = Map.of("idModelo", 20, "nombre", "5055E");

    // Configuración de mocks
    when(estadoRepository.findByNombreEstado(nombreEstado)).thenReturn(Optional.of(estadoMock));
    when(equipoRepository.findByEstado(estadoMock)).thenReturn(List.of(equipoMock));
    when(marcaClient.obtenerMarcaPorId(10)).thenReturn(marcaMock);
    when(modeloClient.obtenerModeloPorId(20)).thenReturn(modeloMock);

    // Ejecutar
    List<Equipo> resultado = estadoService.buscarEquiposPorEstado(nombreEstado);

    // Verificaciones
    assertThat(resultado).hasSize(1);
    assertThat(resultado.get(0).getMarca()).isEqualTo("John Deere");
    assertThat(resultado.get(0).getModelo()).isEqualTo("5055E");
}
    @Test
    void testMostrarTodosLosEstados() {
        Estado estado1 = new Estado(1, "Disponible");
        Estado estado2 = new Estado(2, "En Mantenimiento");

        when(estadoRepository.findAll()).thenReturn(List.of(estado1, estado2)); 
        List<Estado> estados = estadoService.mostrarTodosLosEstados();

        assertThat(estados).hasSize(2);
        assertThat(estados.get(0).getNombreEstado()).isEqualTo("Disponible");

}
    @Test
    void testObtenerEstadoPorId() {
        Estado estadoMock = new Estado(1, "Disponible");

        when(estadoRepository.findById(1)).thenReturn(Optional.of(estadoMock));

        Estado estado = estadoService.obtenerEstadoPorId(1);

        assertThat(estado).isNotNull();
        assertThat(estado.getNombreEstado()).isEqualTo("Disponible");
    }
}
