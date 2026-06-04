package com.example.RolesyPermisos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.RolesyPermisos.model.Role;
import com.example.RolesyPermisos.service.AutorizacionService;
import com.example.RolesyPermisos.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/api/v1/roles")

public class RolController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AutorizacionService autorizacionService;

     @Operation(summary = "Obtener todos los roles", description = "Devuelve la lista completa de roles. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosRoles(@RequestHeader ("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            List<Role> roles = roleService.obtenerTodosLosRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener roles: " + e.getMessage());
        }
    }
    @Operation(summary = "Obtener un rol por ID", description = "Busca y devuelve un rol según su ID. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = Role.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRolPorId(@RequestHeader ("X-User-Id") Integer idUserConectado ,@PathVariable Integer id) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            Role role = roleService.obtenerRolPorId(id);
            return ResponseEntity.ok(role);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener rol: " + e.getMessage());
        }
    }
    @Operation(summary = "Crear un nuevo rol. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearRol(@RequestHeader ("X-User-Id") Integer idUserConectado, @RequestBody Role role) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            Role creado = roleService.guardarRol(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear rol: " + e.getMessage());
        }
    }
    @Operation(summary = "Actualizar un rol existente. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente", 
                     content = @Content(schema = @Schema(implementation = Role.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarRol(@RequestHeader ("X-User-Id") Integer idUserConectado,@PathVariable Integer id, @RequestBody Role roleActualizado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            Role actualizado = roleService.actualizarRol(id, roleActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar rol: " + e.getMessage());
        }
    }
    @Operation(summary = "Eliminar un rol por ID. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rol eliminado correctamente (No Content)"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarRol(@RequestHeader ("X-User-Id") Integer idUserConectado, @PathVariable Integer id) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 1);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            roleService.eliminarRol(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar rol: " + e.getMessage());
        }
    }
    @Operation(summary = "Obtener un rol por nombre. Requiere autorización.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerRolPorNombre(@PathVariable String nombre) {
        try {
            Role role = roleService.obtenerRolPorNombre(nombre);
            return ResponseEntity.ok(role);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener rol por nombre: " + e.getMessage());
        }
    }
}
