package com.example.GestorMarcaYModelo.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.model.Modelo;
import com.example.GestorMarcaYModelo.repository.MarcaRepository;
import com.example.GestorMarcaYModelo.repository.ModeloRepository;

@ExtendWith(MockitoExtension.class)
public class ModeloServiceTest {

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private MarcaRepository marcaRepository;
    
    @InjectMocks
    private ModeloService modeloService;
    
    // test para listar todos los modelos con su marca
    @Test
    void testObtenerTodosLosModelos() {
        List<Modelo> mockModelos = Arrays.asList(new Modelo(1, "Modelo1", null));
        when(modeloRepository.findAll()).thenReturn(mockModelos);
        
        List<Modelo> resultado = modeloService.listarModelos();
        assertThat(resultado).isEqualTo(mockModelos);

}
    @Test
    void eliminarModeloPorId() {
        Modelo mockModelo = new Modelo(1, "Modelo1", null);
        when(modeloRepository.findById(1)).thenReturn(java.util.Optional.of(mockModelo));
        
        modeloService.eliminarModelo(1);

        verify(modeloRepository).delete(mockModelo);
    }


    // test para listar modelos por id
    @Test
    void testObtenerModeloPorId() {
        Modelo mockModelo = new Modelo(1, "Modelo1", null);
        when(modeloRepository.findById(1)).thenReturn(java.util.Optional.of(mockModelo));
        
        Modelo resultado = modeloService.obtenerModeloPorId(1);
        assertThat(resultado).isEqualTo(mockModelo);
    }

    // test para guardar un modelo con una marca existente
    @Test
    void testGuardarModelo() {
    Modelo modeloMock = new Modelo();
    modeloMock.setIdModelo(1);
    modeloMock.setIdMarca(10);
    modeloMock.setNombre("Modelo Test");

    
    Marca marcaMock = new Marca();
    marcaMock.setIdMarca(10);
    marcaMock.setNombre("Marca Test");

    when(marcaRepository.findById(10)).thenReturn(Optional.of(marcaMock));
    when(modeloRepository.save(any(Modelo.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Modelo resultado = modeloService.guardarModelo(modeloMock);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getMarca()).isEqualTo(marcaMock);
    assertThat(resultado.getNombre()).isEqualTo("Modelo Test");
}

    @Test
    void testGuardarModelo_sinIdMarcaLanzaExcepcion() {
        Modelo modelo = new Modelo();
        modelo.setNombre("Modelo sin marca");

        assertThatThrownBy(() -> modeloService.guardarModelo(modelo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("idMarca es obligatorio");
    }

    @Test
    void testGuardarModelo_marcaInexistenteLanzaExcepcion() {
        Modelo modelo = new Modelo();
        modelo.setIdMarca(99);
        modelo.setNombre("Modelo sin marca real");

        when(marcaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> modeloService.guardarModelo(modelo))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class)
                .hasMessageContaining("Marca no encontrada");
    }

    @Test
    void testEliminarModelo_noExisteLanzaExcepcion() {
        when(modeloRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> modeloService.eliminarModelo(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modelo no encontrado");
    }

}

