package com.example.SoporteTecnico.controller;

import com.example.SoporteTecnico.model.Ticket;
import com.example.SoporteTecnico.repository.TicketRepository;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteTecnicoService sopService;

    @MockBean
    private AutorizacionService autorizacionService;

    @MockBean
    private TicketRepository ticketRepository;

    private final Integer idUser = 4; // Rol soporte
    Integer idUserConectado = 123;

    @Test
    void obtenerTickets_returnsOkAndJson() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(1);
        ticket.setFecha_inicio(new Date());
        ticket.setDescripcion("Revisión");
        ticket.setIdUsuario(101);

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());
        when(sopService.getTickets())
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/v1/tickets")
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Revisión"));
    }

    @Test
    void crearTicket_returnsCreatedAndJson() throws Exception {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setDescripcion("Error UI");
        nuevoTicket.setIdUsuario(idUserConectado);
        nuevoTicket.setFecha_inicio(new Date());

        Ticket guardadoTicket = new Ticket();
        guardadoTicket.setId(2);
        guardadoTicket.setDescripcion("Error UI");
        guardadoTicket.setIdUsuario(idUserConectado);
        guardadoTicket.setFecha_inicio(nuevoTicket.getFecha_inicio());

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());
        when(sopService.saveTicket(any(Ticket.class)))
                .thenReturn(guardadoTicket);

        String jsonBody = """
                {
                    "descripcion": "Error UI",
                    "idUsuario": 4
                }
                """;

        mockMvc.perform(post("/api/v1/tickets")
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.descripcion").value("Error UI"));
    }

    @Test
    void eliminarTicket_returnsOk() throws Exception {
        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());
        when(sopService.deleteTicketById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tickets/1")
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket eliminado correctamente."));
    }

    @Test
    void actualizarTicket_returnsOkAndJson() throws Exception {
        Ticket ticketActualizado = new Ticket();
        ticketActualizado.setId(1);
        ticketActualizado.setDescripcion("Detalle actualizado");
        ticketActualizado.setIdUsuario(idUserConectado);
        ticketActualizado.setFecha_inicio(new Date());

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());
        when(sopService.actualizarTicket(eq(1), any(Ticket.class)))
                .thenReturn(ticketActualizado);

        String jsonBody = """
                {
                    "descripcion": "Detalle actualizado",
                    "idUsuario": 4
                }
                """;

        mockMvc.perform(put("/api/v1/tickets/1")
                .header("X-User-Id", idUserConectado)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Detalle actualizado"));
    }

    @Test
    void listarTicketsPorUsuario_returnsOkAndJson() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setId(3);
        ticket.setDescripcion("Consulta resuelta");
        ticket.setIdUsuario(10);
        ticket.setFecha_inicio(new Date());

        when(autorizacionService.validarRol(idUserConectado, 4))
                .thenReturn(ResponseEntity.ok().build());
        when(sopService.getTicketsByUsuarioId(10))
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/v1/tickets/usuario/10")
                .header("X-User-Id", idUserConectado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].descripcion").value("Consulta resuelta"));
    }
}
