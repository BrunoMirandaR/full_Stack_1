package com.example.GestorMarcaYModelo.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.GestorMarcaYModelo.repository.MarcaRepository;
import com.example.GestorMarcaYModelo.repository.ModeloRepository;

@ExtendWith(MockitoExtension.class)
class LoadDatabaseTest {

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModeloRepository modeloRepository;

    @Test
    void initDatabase_shouldSeedDataWhenBothTablesAreEmpty() throws Exception {
        when(marcaRepository.count()).thenReturn(0L);
        when(modeloRepository.count()).thenReturn(0L);

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(marcaRepository, modeloRepository).run();

        verify(marcaRepository, times(3)).save(org.mockito.ArgumentMatchers.any());
        verify(modeloRepository, times(8)).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void initDatabase_shouldSkipWhenDataAlreadyExists() throws Exception {
        when(marcaRepository.count()).thenReturn(1L);
        lenient().when(modeloRepository.count()).thenReturn(0L);

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(marcaRepository, modeloRepository).run();

        verify(marcaRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(modeloRepository, never()).save(org.mockito.ArgumentMatchers.any());
        assertThat(loadDatabase).isNotNull();
    }
}