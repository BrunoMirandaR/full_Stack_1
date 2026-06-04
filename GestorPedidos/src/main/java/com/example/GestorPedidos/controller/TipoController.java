package com.example.GestorPedidos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestorPedidos.model.Tipo;
import com.example.GestorPedidos.service.AutorizacionService;
import com.example.GestorPedidos.service.TipoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/tipos")
public class TipoController {
    private final TipoService tipoService;
    private final AutorizacionService autorizacionService;

    public TipoController(TipoService tipoService, AutorizacionService autorizacionService) {
        this.tipoService = tipoService;
        this.autorizacionService = autorizacionService;
    }

    @Operation(summary = "Devuelve una lista con todos los tipos de pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos encontrados", content = @Content(schema = @Schema(implementation = Tipo.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> mostrarTodos(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            List<Tipo> tipos = tipoService.mostrarTodos();
            return ResponseEntity.ok(tipos); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();// 500 Internal Server Error

        }
    }

    @Operation(summary = "Crea un nuevo tipo de pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo creado correctamente", content = @Content(schema = @Schema(implementation = Tipo.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearTipo(@RequestHeader("X-User-Id") Integer idUserConectado, @RequestBody Tipo tipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Tipo nuevoTipo = tipoService.crearTipo(tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtiene un tipo de pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo encontrado", content = @Content(schema = @Schema(implementation = Tipo.class))),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idTipo}")
    public ResponseEntity<?> obtenerPorId(@RequestHeader("X-User-Id") Integer idUserConectado, @PathVariable Integer idTipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Tipo tipo = tipoService.obtenerPorId(idTipo);
            return ResponseEntity.ok(tipo); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @Operation(summary = "Actualiza un tipo de pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo actualizado correctamente", content = @Content(schema = @Schema(implementation = Tipo.class))),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @PutMapping("/modificar/{idTipo}")
    public ResponseEntity<?> actualizarTipo(@RequestHeader("X-User-Id") Integer idUserConectado, @PathVariable Integer idTipo, @RequestBody Tipo tipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Tipo tipoActualizado = tipoService.actualizarTipo(idTipo, tipo);
            return ResponseEntity.ok(tipoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Elimina un tipo de pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    @DeleteMapping("/{idTipo}")
    public ResponseEntity<?> eliminarTipo(@RequestHeader("X-User-Id") Integer idUserConectado, @PathVariable Integer idTipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            tipoService.eliminarTipo(idTipo);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
