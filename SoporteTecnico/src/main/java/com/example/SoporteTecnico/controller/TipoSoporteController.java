package com.example.SoporteTecnico.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SoporteTecnico.model.TipoSoporte;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/tiposSoporte")
@Tag(name = "Tipos de Soporte", description = "Operaciones para gestionar los tipos de soporte técnico")
public class TipoSoporteController {

    @Autowired
    private SoporteTecnicoService sopService;

    @Autowired
    private AutorizacionService autorizacionService;

    @Operation(summary = "Crear un nuevo tipo de soporte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de soporte creado correctamente", 
                     content = @Content(schema = @Schema(implementation = TipoSoporte.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno al crear tipo de soporte")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearTipoSoporte(
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @RequestBody TipoSoporte nuevoTipo) {
        
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            TipoSoporte ts = sopService.createTipoSoporte(nuevoTipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(ts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear TipoSoporte: " + e.getMessage());
        }
    }

    @Operation(summary = "Modificar un tipo de soporte existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de soporte actualizado correctamente",
                     content = @Content(schema = @Schema(implementation = TipoSoporte.class))),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Tipo de soporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno al actualizar tipo de soporte")
    })
    @PutMapping("modificar/{id}")
    public ResponseEntity<?> editarTipoSoporte(
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @PathVariable Integer id,
        @RequestBody TipoSoporte tipoSoporte) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        try {
            TipoSoporte actualizado = sopService.updateTipoSoporte(id, tipoSoporte);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar TipoSoporte: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todos los tipos de soporte")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipos de soporte encontrados",
                     content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoSoporte.class)))),
        @ApiResponse(responseCode = "204", description = "No hay tipos de soporte registrados"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> listarTiposSoporte(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            List<TipoSoporte> lista = sopService.getTipoSoportes();
            if (lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener los tipos de soporte");
        }
    }

    @Operation(summary = "Eliminar un tipo de soporte por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de soporte eliminado correctamente"),
        @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Tipo de soporte no encontrado")
    })
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarTipoSoporte(
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @PathVariable Integer id) {
        
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        boolean deleted = sopService.deleteTipoSoporteById(id);
        if (deleted) {
            return ResponseEntity.ok("TipoSoporte eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TipoSoporte no encontrado.");
        }
    }
}
