package com.example.GestorPedidos.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.GestorPedidos.model.Tipo;
import com.example.GestorPedidos.repository.TipoRepository;

@ExtendWith(MockitoExtension.class)
public class TipoServiceTest {
    @Mock
    private TipoRepository tipoRepository;

    @InjectMocks
    private TipoService tipoService;

    private Tipo tipo1;
    private Tipo tipo2;
    
    @BeforeEach
    void setup() {
        tipo1 = new Tipo();
        tipo1.setIdTipo(1);
        tipo1.setNombre("Presencial");

        tipo2 = new Tipo();
        tipo2.setIdTipo(2);
        tipo2.setNombre("Online");
    }

    @Test
    void testObtenerPorId() {
        when(tipoRepository.findById(1)).thenReturn(Optional.of(tipo1));

        Tipo resultado = tipoService.obtenerPorId(1);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdTipo()).isEqualTo(1);
        assertThat(resultado.getNombre()).isEqualTo("Presencial");
    }

    @Test
    void testMostrarTodos() {
        when(tipoRepository.findAll()).thenReturn(Arrays.asList(tipo1, tipo2));

        List<Tipo> tipos = tipoService.mostrarTodos();

        assertThat(tipos).hasSize(2);
        assertThat(tipos).extracting(Tipo::getNombre).contains("Presencial", "Online");
    }

    @Test
    void testCrearTipo() {
        Tipo nuevoTipo = new Tipo();
        nuevoTipo.setNombre("Express");

        Tipo tipoGuardado = new Tipo();
        tipoGuardado.setIdTipo(3);
        tipoGuardado.setNombre("Express");

        when(tipoRepository.save(nuevoTipo)).thenReturn(tipoGuardado);

        Tipo resultado = tipoService.crearTipo(nuevoTipo);

        assertThat(resultado.getIdTipo()).isEqualTo(3);
        assertThat(resultado.getNombre()).isEqualTo("Express");
    }

    @Test
    void testActualizarTipo() {
        Tipo actualizado = new Tipo();
        actualizado.setNombre("Programado");

        when(tipoRepository.findById(1)).thenReturn(Optional.of(tipo1));
        when(tipoRepository.save(any(Tipo.class))).thenAnswer(inv -> inv.getArgument(0));

        Tipo resultado = tipoService.actualizarTipo(1, actualizado);

        assertThat(resultado.getNombre()).isEqualTo("Programado");
        assertThat(resultado.getIdTipo()).isEqualTo(1);
    }

    @Test
    void testEliminarTipo() {
        when(tipoRepository.existsById(1)).thenReturn(true);

        tipoService.eliminarTipo(1);

        verify(tipoRepository, times(1)).deleteById(1);
    }
}
