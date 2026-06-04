package com.example.GestorInventario.controller;

import java.util.List;
import java.util.Set;

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

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
import com.example.GestorInventario.service.AutorizacionService;
import com.example.GestorInventario.service.EquipoService;
import com.example.GestorInventario.service.EstadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("api/v1/equipos")
public class EquipoController {

    private final EquipoService equipoService;
    private final EstadoService estadoService;
    private final AutorizacionService autorizacionService;

    public EquipoController(EquipoService equipoService, EstadoService estadoService,
            AutorizacionService autorizacionService) {
        this.estadoService = estadoService;
        this.equipoService = equipoService;
        this.autorizacionService = autorizacionService;
    }

    // mostrar todos los equipos
    @Operation(summary = "Mostrar la lista con todos los equipos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos encontrados",content = @Content(schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron equipos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> mostrarTodosLosEquipos(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 2);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse; // 403 o 401 según corresponda
            }
            List<Equipo> equipos = equipoService.mostrarTodosLosEquipos();

            if (equipos == null || equipos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
            }
            return ResponseEntity.ok(equipos); // 200 OK
        } catch (Exception e) {
            // Aquí podrías loguear el error para debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    // obtener equipo por id
    @Operation(summary = "Obtener un equipo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado", content = @Content(schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idEquipo}")
    public ResponseEntity<?> obtenerEquipoPorId(@PathVariable Integer idEquipo) {
        try {
            Equipo equipo = equipoService.obtenerEquipoPorId(idEquipo);
            if (equipo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(equipo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Guardar un equipo
    @Operation(summary = "Ingresar un nuevo equipo al sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado correctamente", content = @Content(schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Marca, modelo o estado no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/guardar")
    public ResponseEntity<?> guardarEquipo(@RequestHeader("X-User-Id") Integer idUserConectado,
            @RequestBody Equipo equipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 2);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            Equipo creado = equipoService.guardarEquipo(equipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Buscar equipos por estado
    @Operation(summary = "Buscar equipos por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos encontrados", content = @Content(schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado para el estado dado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{nombreEstado}")
    public ResponseEntity<?> buscarEquiposPorEstado(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable String nombreEstado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 2);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            List<Equipo> equipos = estadoService.buscarEquiposPorEstado(nombreEstado);
            if (equipos == null || equipos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(equipos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar un equipo por id
    @Operation(summary = "Eliminar un equipo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarEquipo(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer id) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 2);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            equipoService.eliminarEquipo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener todos los estados locales
    @Operation(summary = "Obtener una lista de todos los estados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados obtenida correctamente", content = @Content(schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estados")
    public ResponseEntity<?> obtenerEstados(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of( 2, 3));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            return ResponseEntity.ok(estadoService.mostrarTodosLosEstados());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Obtener un estado por id
    @Operation(summary = "Obtener un estado por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado", content = @Content(schema = @Schema(implementation = Estado.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Estado no encontrado")
    })
    @GetMapping("/estados/{idEstado}")
    public ResponseEntity<?> obtenerEstadoPorId(@PathVariable Integer idEstado) {
        try {
            Estado estado = estadoService.obtenerEstadoPorId(idEstado);
            if (estado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(estado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Modificar un equipo por id
    @Operation(summary = "Modificar un equipo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo modificado correctamente", content = @Content(schema = @Schema(implementation = Equipo.class))),
            @ApiResponse(responseCode = "401", description = "Acceso Denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado.Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PutMapping("/modificar/{idEquipo}")
    public ResponseEntity<?> modificarEquipo(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idEquipo,
            @RequestBody Equipo equipo) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 2);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            Equipo equipoModificado = equipoService.modificarEquipo(idEquipo, equipo);
            if (equipoModificado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(equipoModificado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
