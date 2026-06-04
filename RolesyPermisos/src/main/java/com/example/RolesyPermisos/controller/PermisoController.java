package com.example.RolesyPermisos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RolesyPermisos.model.Permiso;
import com.example.RolesyPermisos.service.AutorizacionService;
import com.example.RolesyPermisos.service.PermisoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/permisos")
public class PermisoController {
    private final PermisoService permisoService;
    private final AutorizacionService autorizacionService;

    public PermisoController(PermisoService permisoService, AutorizacionService autorizacionService) {
        this.permisoService = permisoService;
        this.autorizacionService = autorizacionService;
    }
    @Operation(summary = "Obtener todos los permisos. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Permiso.class)))
    })
    @GetMapping()
    public ResponseEntity<?> obtenerTodosLosPermisos(@RequestHeader ("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            List<Permiso> permisos = permisoService.obtenerTodosLosPermisos();
            return ResponseEntity.ok(permisos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al obtener permisos: " + e.getMessage());
        }
    }

    // Mostrar permiso por id
    @Operation(summary = "Obtener un permiso por ID. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Permiso.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPermisoPorId(@RequestHeader ("X-User-Id") Integer idUserConectado, @PathVariable Integer id) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            Permiso permiso = permisoService.obtenerPermisoPorId(id);
            return ResponseEntity.ok(permiso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al obtener permiso: " + e.getMessage());
        }
    }

    // Modificar un permiso por id
    @Operation(summary = "Modificar un permiso por ID. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso modificado correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Permiso.class)))
    })
    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificarPermiso(@RequestHeader ("X-User-Id") Integer idUserConectado, @PathVariable Integer id, @RequestBody Permiso permiso) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            Permiso permisoExistente = permisoService.obtenerPermisoPorId(id);
            permiso.setIdPermiso(permisoExistente.getIdPermiso());
            Permiso actualizado = permisoService.guardarPermiso(permiso);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al modificar permiso: " + e.getMessage());
        }
    }

    // Eliminar un permiso por id
    @Operation(summary = "Eliminar un permiso por ID.Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Permiso eliminado correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Permiso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Permiso.class)))
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPermiso(@RequestHeader ("X-User-Id") Integer idUserConectado, @PathVariable Integer id) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            permisoService.eliminarPermisoPorId(id);
            return ResponseEntity.ok("Permiso eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al eliminar permiso: " + e.getMessage());
        }
    }
    


}
