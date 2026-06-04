package com.example.GestorEntregas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;

import com.example.GestorEntregas.model.Entrega;
import com.example.GestorEntregas.service.AutorizacionService;
import com.example.GestorEntregas.service.EntregaService;
import com.example.GestorEntregas.webclient.EstadoClient;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/entregas")
public class EntregaController {
    private final EntregaService entregaService;
    private final AutorizacionService autorizacionService;

    public EntregaController(EntregaService entregaService, EstadoClient estadoClient,
            AutorizacionService autorizacionService) {
        this.entregaService = entregaService;
        this.autorizacionService = autorizacionService;
    }

    @Operation(summary = "Crea una nueva entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entrega creada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Entrega.class)))
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearEntrega(@RequestHeader("X-User-Id") Integer idUserConectado,
            @RequestBody Entrega entrega) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado,
                    3); // 3 es el rol de logistica
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            if (entrega.getIdEntrega() != null) {
                return ResponseEntity.badRequest().body("No debes enviar el id al crear una nueva entrega");
            }
            Entrega nuevaEntrega = entregaService.crearEntrega(entrega, idUserConectado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEntrega); // 201
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 si hay error de lógica
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // metodo para obtener todas las entregas
    @Operation(summary = "Obtiene todas las entregas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entregas obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron entregas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrega.class)))
    })
    @GetMapping()
    public ResponseEntity<?> obtenerTodasLasEntregas(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            // Validar si el usuario tiene el rol de logistica
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado,
                    3); // 3 es el rol de logistica
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            List<Entrega> entregas = entregaService.obtenerTodasLasEntregas();
            if (entregas == null || entregas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron entregas");
            }
            return ResponseEntity.ok(entregas); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // metodo para actualizar el estado de una entrega
    @Operation(summary = "Actualiza el estado de una entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado de la entrega actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "Entrega no encontrada o estado inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrega.class)))
    })
    @PutMapping("/actualizar/{idEntrega}")
    public ResponseEntity<?> actualizarEstadoEntrega(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idEntrega,
            @RequestBody Map<String, String> body) {
        try {
            // Validar si el usuario tiene el rol de logistica
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado,
                    3); // 3 es el rol de logistica
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            String nuevoEstado = body.get("nuevoEstado");
            if (nuevoEstado == null || nuevoEstado.isEmpty()) {
                return ResponseEntity.badRequest().body("El nuevo estado es requerido");
            }
            Entrega entregaActualizada = entregaService.actualizarEstadoEntrega(idEntrega, nuevoEstado,
                    idUserConectado);
            return ResponseEntity.ok(entregaActualizada); // 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 si no existe entrega o
                                                                                     // estado inválido
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // metodo para eliminar entrega por id
    @Operation(summary = "Elimina una entrega por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entrega eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Prohibido"),
            @ApiResponse(responseCode = "404", description = "Entrega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrega.class)))
    })
    @DeleteMapping("/eliminar/{idEntrega}")
    public ResponseEntity<?> eliminarEntrega(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idEntrega) {
        try {
            // Validar si el usuario tiene el rol de logistica
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado,
                    3); // 3 es el rol de logistica
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            entregaService.eliminarEntrega(idEntrega);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 si no existe entrega
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
}