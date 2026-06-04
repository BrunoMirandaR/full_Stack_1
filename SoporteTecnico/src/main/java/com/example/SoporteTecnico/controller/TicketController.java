package com.example.SoporteTecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SoporteTecnico.model.Ticket;
import com.example.SoporteTecnico.repository.TicketRepository;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/v1")
@Tag(name = "Tickets", description = "Operaciones relacionadas con tickets de soporte técnico")
public class TicketController {

    @Autowired 
    private SoporteTecnicoService sopService;
    @Autowired
    private AutorizacionService autorizacionService;
    @Autowired
    private TicketRepository ticketRepository;

    @Operation(summary = "Obtiene todos los tickets de soporte técnico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tickets devuelta correctamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron tickets"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
    @GetMapping("/tickets")
    public ResponseEntity<?> obtenerTickets(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        List<Ticket> tickets = sopService.getTickets();
        if(tickets.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Crear un nuevo ticket de soporte técnico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ticket creado correctamente"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Error relacionado con datos del ticket"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/tickets/crear")
    public ResponseEntity<?> crearProyecto(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado, 
        @Parameter(description = "Datos del ticket a crear", required = true)
        @RequestBody Ticket nuevoTicket) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }

        try {
            Ticket tk = sopService.saveTicket(nuevoTicket);
            return ResponseEntity.status(201).body(tk);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al crear Ticket: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un ticket de soporte técnico por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    @DeleteMapping("tickets/eliminar/{id}")
    public ResponseEntity<?> deleteTicket(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado, 
        @Parameter(description = "ID del ticket a eliminar", required = true)
        @PathVariable Integer id) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }

        boolean deleted = sopService.deleteTicketById(id);
        if (deleted) {
            return ResponseEntity.ok("Ticket eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado.");
        }
    }

    @Operation(summary = "Actualizar un ticket de soporte técnico por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "ID inválido o datos inválidos"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/tickets/modificar/{id}")
    public ResponseEntity<?> actualizarTicket(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado, 
        @Parameter(description = "ID del ticket a actualizar", required = true)
        @PathVariable Integer id, 
        @Parameter(description = "Datos actualizados del ticket", required = true)
        @RequestBody Ticket ticket) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        if (id == null || id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de ticket no válido");
        }

        try {
            Ticket actualizado = sopService.actualizarTicket(id, ticket);
            return ResponseEntity.ok(actualizado);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket no encontrado");

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos para el ticket");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el ticket");
        }
    }

    @Operation(summary = "Listar tickets de soporte técnico por ID de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tickets devuelta correctamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron tickets para el usuario"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
    @GetMapping("/tickets/usuario/{id}")
    public ResponseEntity<?> listarTicketsPorUsuario(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado, 
        @Parameter(description = "ID del usuario para filtrar tickets", required = true)
        @PathVariable Integer id) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        List<Ticket> tickets = sopService.getTicketsByUsuarioId(id);
        if (tickets == null || tickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tickets);
    }
}
