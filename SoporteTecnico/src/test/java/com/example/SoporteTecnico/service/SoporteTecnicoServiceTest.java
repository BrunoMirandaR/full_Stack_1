package com.example.SoporteTecnico.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import jakarta.persistence.EntityNotFoundException;

import com.example.SoporteTecnico.model.Soporte;
import com.example.SoporteTecnico.model.Ticket;
import com.example.SoporteTecnico.model.TipoSoporte;
import com.example.SoporteTecnico.repository.SoporteRepository;
import com.example.SoporteTecnico.repository.TicketRepository;
import com.example.SoporteTecnico.repository.TipoSoporteRepository;
import com.example.SoporteTecnico.webclient.UsuarioUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SoporteTecnicoServiceTest {

    @Mock
    private SoporteRepository soporteRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TipoSoporteRepository tipoSoporteRepository;

    @Mock
    private UsuarioUser usuarioUser;

    @InjectMocks
    private SoporteTecnicoService soporteTecnicoService;

    private Ticket ticket;
    private Soporte soporte;
    private TipoSoporte tipoSoporte;

    @BeforeEach
    void setup() {
        ticket = new Ticket();
        ticket.setId(1);
        ticket.setIdUsuario(100);
        ticket.setDescripcion("Ticket test");
        ticket.setFecha_inicio(new Date());

        soporte = new Soporte();
        soporte.setId(1);
        soporte.setObservacion("Observación test");
        soporte.setFecha_soporte(new Date());
        soporte.setTicket(ticket);

        tipoSoporte = new TipoSoporte();
        tipoSoporte.setId(1);
        tipoSoporte.setNombre("TipoTest");
        tipoSoporte.setTicket(ticket);
    }

    @Test
    void testSaveTicket_Success() {
        // Mock usuarioUser to return a non-empty map indicating user exists
        when(usuarioUser.getUsuarioPorId(ticket.getIdUsuario()))
            .thenReturn(Map.of("id", ticket.getIdUsuario(), "nombre", "UsuarioTest"));

        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket resultado = soporteTecnicoService.saveTicket(ticket);

        assertThat(resultado).isEqualTo(ticket);
        verify(usuarioUser).getUsuarioPorId(ticket.getIdUsuario());
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testSaveTicket_UsuarioNoEncontrado() {
        when(usuarioUser.getUsuarioPorId(ticket.getIdUsuario())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            soporteTecnicoService.saveTicket(ticket);
        });

        assertThat(exception.getMessage()).isEqualTo("usuario no encontrado");
        verify(usuarioUser).getUsuarioPorId(ticket.getIdUsuario());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void testActualizarTicket_Success() {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setIdUsuario(100);
        nuevoTicket.setFecha_inicio(new Date());
        nuevoTicket.setDescripcion("Actualizado");

        when(usuarioUser.getUsuarioPorId(100)).thenReturn(Map.of("id", 100));
        when(ticketRepository.findById(1)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));

        Ticket actualizado = soporteTecnicoService.actualizarTicket(1, nuevoTicket);

        assertThat(actualizado.getDescripcion()).isEqualTo("Actualizado");
        verify(ticketRepository).findById(1);
        verify(usuarioUser).getUsuarioPorId(100);
        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void testActualizarTicket_TicketNoEncontrado() {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setIdUsuario(100);
        nuevoTicket.setFecha_inicio(new Date());

        when(usuarioUser.getUsuarioPorId(100)).thenReturn(Map.of("id", 100));
        when(ticketRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            soporteTecnicoService.actualizarTicket(1, nuevoTicket);
        });

        assertThat(exception.getMessage()).contains("Ticket con ID 1 no encontrado");
    }

    @Test
    void testActualizarTicket_UsuarioNoExiste() {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setIdUsuario(100);
        nuevoTicket.setFecha_inicio(new Date());

        when(usuarioUser.getUsuarioPorId(100)).thenReturn(Collections.emptyMap());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            soporteTecnicoService.actualizarTicket(1, nuevoTicket);
        });

        assertThat(exception.getMessage()).contains("El usuario con ID 100 no existe");
    }

    @Test
    void testCreateSoporte() {
        when(soporteRepository.save(soporte)).thenReturn(soporte);

        Soporte creado = soporteTecnicoService.createSoporte(soporte);

        assertThat(creado).isEqualTo(soporte);
        verify(soporteRepository).save(soporte);
    }

    @Test
    void testDeleteSoporteById_Exists() {
        when(soporteRepository.existsById(1)).thenReturn(true);
        doNothing().when(soporteRepository).deleteById(1);

        boolean result = soporteTecnicoService.deleteSoporteById(1);

        assertThat(result).isTrue();
        verify(soporteRepository).deleteById(1);
    }

    @Test
    void testDeleteSoporteById_NotExists() {
        when(soporteRepository.existsById(1)).thenReturn(false);

        boolean result = soporteTecnicoService.deleteSoporteById(1);

        assertThat(result).isFalse();
        verify(soporteRepository, never()).deleteById(anyInt());
    }

    @Test
    void testGetTicketsByUsuarioId() {
        List<Ticket> tickets = List.of(ticket);
        when(ticketRepository.findByIdUsuario(100)).thenReturn(tickets);

        List<Ticket> resultado = soporteTecnicoService.getTicketsByUsuarioId(100);

        assertThat(resultado).isEqualTo(tickets);
        verify(ticketRepository).findByIdUsuario(100);
    }

    @Test
    void testUpdateTipoSoporte_Success() {
        TipoSoporte nuevoTipo = new TipoSoporte();
        nuevoTipo.setNombre("NuevoNombre");

        when(tipoSoporteRepository.findById(1)).thenReturn(Optional.of(tipoSoporte));
        when(tipoSoporteRepository.save(any(TipoSoporte.class))).thenAnswer(i -> i.getArgument(0));

        TipoSoporte actualizado = soporteTecnicoService.updateTipoSoporte(1, nuevoTipo);

        assertThat(actualizado.getNombre()).isEqualTo("NuevoNombre");
        verify(tipoSoporteRepository).findById(1);
        verify(tipoSoporteRepository).save(any(TipoSoporte.class));
    }
}
