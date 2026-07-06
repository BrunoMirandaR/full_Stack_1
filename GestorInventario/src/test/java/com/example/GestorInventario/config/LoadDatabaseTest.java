package com.example.GestorInventario.config;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.CommandLineRunner;

import com.example.GestorInventario.repository.EquipoRepository;
import com.example.GestorInventario.repository.EstadoRepository;
import com.example.GestorInventario.webclient.MarcaClient;
import com.example.GestorInventario.webclient.ModeloClient;

class LoadDatabaseTest {

    @Test
    void initDatabase_shouldSaveDataWhenTablesAreEmpty() throws Exception {
        EquipoRepository equipoRepository = Mockito.mock(EquipoRepository.class);
        EstadoRepository estadoRepository = Mockito.mock(EstadoRepository.class);
        MarcaClient marcaClient = Mockito.mock(MarcaClient.class);
        ModeloClient modeloClient = Mockito.mock(ModeloClient.class);

        when(equipoRepository.count()).thenReturn(0L);
        when(estadoRepository.count()).thenReturn(0L);
        when(estadoRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(equipoRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(marcaClient.obtenerTodasLasMarcas()).thenReturn(List.of(Map.of("idMarca", 1)));
        when(modeloClient.obtenerTodosLosModelos()).thenReturn(List.of(Map.of("idModelo", 1)));
        when(marcaClient.obtenerMarcaPorId(1)).thenReturn(Map.of("idMarca", 1, "nombre", "John Deere"));
        when(marcaClient.obtenerMarcaPorId(2)).thenReturn(Map.of("idMarca", 2, "nombre", "New Holland"));
        when(marcaClient.obtenerMarcaPorId(3)).thenReturn(Map.of("idMarca", 3, "nombre", "Case IH"));
        when(modeloClient.obtenerModeloPorId(1)).thenReturn(Map.of("idModelo", 1, "nombre", "5055E"));
        when(modeloClient.obtenerModeloPorId(2)).thenReturn(Map.of("idModelo", 2, "nombre", "S680"));
        when(modeloClient.obtenerModeloPorId(3)).thenReturn(Map.of("idModelo", 3, "nombre", "T6050"));
        when(modeloClient.obtenerModeloPorId(4)).thenReturn(Map.of("idModelo", 4, "nombre", "CR7.90"));
        when(modeloClient.obtenerModeloPorId(5)).thenReturn(Map.of("idModelo", 5, "nombre", "Puma 185"));
        when(modeloClient.obtenerModeloPorId(6)).thenReturn(Map.of("idModelo", 6, "nombre", "1230"));
        when(modeloClient.obtenerModeloPorId(7)).thenReturn(Map.of("idModelo", 7, "nombre", "R4045"));

        LoadDatabase loadDatabase = new LoadDatabase();
        CommandLineRunner runner = loadDatabase.initDatabase(equipoRepository, estadoRepository, marcaClient, modeloClient);

        runner.run();

        verify(estadoRepository).saveAll(any());
        verify(equipoRepository).saveAll(any());
    }

    @Test
    void initDatabase_shouldSkipWhenDataAlreadyExists() throws Exception {
        EquipoRepository equipoRepository = Mockito.mock(EquipoRepository.class);
        EstadoRepository estadoRepository = Mockito.mock(EstadoRepository.class);
        MarcaClient marcaClient = Mockito.mock(MarcaClient.class);
        ModeloClient modeloClient = Mockito.mock(ModeloClient.class);

        when(equipoRepository.count()).thenReturn(1L);
        when(estadoRepository.count()).thenReturn(1L);

        LoadDatabase loadDatabase = new LoadDatabase();
        CommandLineRunner runner = loadDatabase.initDatabase(equipoRepository, estadoRepository, marcaClient, modeloClient);

        runner.run();

        verify(marcaClient, never()).obtenerTodasLasMarcas();
        verify(modeloClient, never()).obtenerTodosLosModelos();
        verify(equipoRepository, never()).saveAll(any());
    }

    @Test
    void initDatabase_shouldFailWhenRoleIdTypeIsUnsupported() {
        EquipoRepository equipoRepository = Mockito.mock(EquipoRepository.class);
        EstadoRepository estadoRepository = Mockito.mock(EstadoRepository.class);
        MarcaClient marcaClient = Mockito.mock(MarcaClient.class);
        ModeloClient modeloClient = Mockito.mock(ModeloClient.class);

        when(equipoRepository.count()).thenReturn(0L);
        when(estadoRepository.count()).thenReturn(0L);
        when(estadoRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(marcaClient.obtenerTodasLasMarcas()).thenReturn(List.of(Map.of("idMarca", 1)));
        when(modeloClient.obtenerTodosLosModelos()).thenReturn(List.of(Map.of("idModelo", 1)));
        when(marcaClient.obtenerMarcaPorId(1)).thenReturn(Map.of("idMarca", true));

        LoadDatabase loadDatabase = new LoadDatabase();
        CommandLineRunner runner = loadDatabase.initDatabase(equipoRepository, estadoRepository, marcaClient, modeloClient);

        assertThatThrownBy(runner::run)
                .isInstanceOf(ClassCastException.class);
    }
}