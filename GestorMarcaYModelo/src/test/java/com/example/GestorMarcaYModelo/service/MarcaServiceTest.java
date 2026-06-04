package com.example.GestorMarcaYModelo.service;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

     }   
}