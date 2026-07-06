package com.example.GestorMarcaYModelo.service;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.repository.MarcaRepository;

@ExtendWith(MockitoExtension.class)
public class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private MarcaService marcaService;
    // test para listar todas las marcas
    @Test
    void testObtenerTodasLasMarcas() {
        List<Marca> mockmarcas = Arrays.asList(new Marca(1, "Marca1"));
        when(marcaRepository.findAll()).thenReturn(mockmarcas);

        List<Marca> resultado = marcaService.listarMarcas();

        assertThat(resultado).isEqualTo(mockmarcas);
    }
    // test para obtener una marca por id
    @Test
    void testObtenerMarcaPorId() {
        Marca mockMarca = new Marca(1, "Marca1");
        when(marcaRepository.findById(1)).thenReturn(java.util.Optional.of(mockMarca));

        Marca resultado = marcaService.obtenerMarcaPorId(1);

        assertThat(resultado).isEqualTo(mockMarca);
    }
    // test para guardar una marca
    @Test
    void testGuardarMarca() {
        Marca nuevaMarca = new Marca(1, "Marca1");
        when(marcaRepository.save(nuevaMarca)).thenReturn(nuevaMarca);

        Marca resultado = marcaService.guardarMarca(nuevaMarca);

        assertThat(resultado).isEqualTo(nuevaMarca);
}
    // test para eliminar una marca por id
    @Test
    void testEliminarMarca() {
        Integer idMarca = 1;
        when(marcaRepository.existsById(idMarca)).thenReturn(true);

        marcaService.eliminarMarca(idMarca);
        verify(marcaRepository).deleteById(idMarca);

     }

    @Test
    void testGuardarMarca_sinNombreLanzaExcepcion() {
        Marca marcaSinNombre = new Marca();

        assertThatThrownBy(() -> marcaService.guardarMarca(marcaSinNombre))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("obligatorio");
    }

    @Test
    void testObtenerMarcaPorId_noExisteLanzaExcepcion() {
        when(marcaRepository.findById(99)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> marcaService.obtenerMarcaPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Marca no encontrada");
    }

    @Test
    void testEliminarMarca_idNuloLanzaExcepcion() {
        assertThatThrownBy(() -> marcaService.eliminarMarca(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede ser nulo");
    }

    @Test
    void testEliminarMarca_noExisteLanzaExcepcion() {
        when(marcaRepository.existsById(99)).thenReturn(false);

        assertThatThrownBy(() -> marcaService.eliminarMarca(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Marca no encontrada");
    }

    @Test
    void testGuardarMarca_errorDeBaseDeDatos() {
        Marca marca = new Marca(1, "Marca1");
        doThrow(new DataAccessResourceFailureException("DB caída")).when(marcaRepository).save(marca);

        assertThatThrownBy(() -> marcaService.guardarMarca(marca))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al guardar la marca");
    }

    @Test
    void testEliminarMarca_errorDeBaseDeDatos() {
        when(marcaRepository.existsById(1)).thenReturn(true);
        doThrow(new DataAccessResourceFailureException("DB caída")).when(marcaRepository).deleteById(1);

        assertThatThrownBy(() -> marcaService.eliminarMarca(1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al eliminar la marca");
    }
}