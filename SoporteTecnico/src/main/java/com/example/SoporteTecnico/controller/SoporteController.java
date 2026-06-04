package com.example.SoporteTecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SoporteTecnico.model.Soporte;
import com.example.SoporteTecnico.service.AutorizacionService;
import com.example.SoporteTecnico.service.SoporteTecnicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/soportes")
@Tag(name = "Soporte Técnico", description = "Operaciones relacionadas con soportes técnicos")
public class SoporteController {

    @Autowired
    private SoporteTecnicoService sopService;
    
    @Autowired
    private AutorizacionService autorizacionService;

    @Operation(summary = "Crear un nuevo soporte técnico",
               description = "Crea un nuevo registro de soporte técnico si el usuario tiene el rol adecuado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Soporte creado correctamente"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearSoporte(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @Parameter(description = "Datos del soporte a crear", required = true)
        @RequestBody Soporte nuevoSoporte) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Soporte soporte = sopService.createSoporte(nuevoSoporte);
            return ResponseEntity.status(HttpStatus.CREATED).body(soporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear soporte: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Editar un soporte técnico existente",
               description = "Actualiza los datos de un soporte técnico existente si el usuario tiene el rol adecuado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Soporte actualizado correctamente"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> editarSoporte(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @Parameter(description = "ID del soporte a modificar", required = true)
        @PathVariable Integer id,
        @Parameter(description = "Datos actualizados del soporte", required = true)
        @RequestBody Soporte soporte) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Soporte actualizado = sopService.updateSoporte(id, soporte);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar soporte: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Listar todos los soportes técnicos",
               description = "Obtiene la lista completa de soportes técnicos si el usuario tiene el rol adecuado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de soportes devuelta correctamente"),
        @ApiResponse(responseCode = "204", description = "No se encontraron soportes"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado")
    })
    @GetMapping
    public ResponseEntity<?> listarSoportes(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado) {
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        List<Soporte> lista = sopService.getSoportes();
        if (lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }
    
    @Operation(summary = "Eliminar un soporte técnico",
               description = "Elimina un soporte técnico por ID si el usuario tiene el rol adecuado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Soporte eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
        @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarSoporte(
        @Parameter(description = "ID del usuario conectado para validar permisos", required = true)
        @RequestHeader("X-User-Id") Integer idUserConectado,
        @Parameter(description = "ID del soporte a eliminar", required = true)
        @PathVariable Integer id) {
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 4);
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }
        boolean deleted = sopService.deleteSoporteById(id);
        if (deleted) {
            return ResponseEntity.ok("Soporte eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Soporte no encontrado.");
        }
    }
}
