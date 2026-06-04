package com.example.GestorPedidos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.example.GestorPedidos.model.Pedido;
import com.example.GestorPedidos.service.AutorizacionService;
import com.example.GestorPedidos.service.PedidoService;
import com.example.GestorPedidos.webclient.UsuarioClient;
import com.example.GestorPedidos.webclient.UsuarioConectadoClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioClient usuarioClient;
    private final UsuarioConectadoClient usuarioConectadoClient;
    private final AutorizacionService autorizacionService;

    public PedidoController(PedidoService pedidoService, UsuarioClient usuarioClient,
            UsuarioConectadoClient usuarioConectadoClient, AutorizacionService autorizacionService) {
        this.pedidoService = pedidoService;
        this.usuarioClient = usuarioClient;
        this.usuarioConectadoClient = usuarioConectadoClient;
        this.autorizacionService = autorizacionService;
    }

    // crear un nuevo pedido
    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pedido.class)))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<?> crearPedido(@RequestHeader("X-User-Id") Integer idUserConectado,
                                    @RequestBody Map<String, Object> body) {
    try {
        // Validar rol del usuario conectado 
        ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
        if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
            return autorizacionResponse;
        }

        // Obtener ID real del usuario conectado desde el microservicio de sesiones
        Optional<Map<String, Object>> conectadoOpt = usuarioConectadoClient.buscarUsuarioConectadoPorId(idUserConectado);
        if (conectadoOpt.isEmpty() || !conectadoOpt.get().containsKey("userId")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no conectado.");
        }
        Integer idUsuario = (Integer) conectadoOpt.get().get("userId");

        // Obtener los datos del usuario real
        Optional<Map<String, Object>> usuarioOpt = usuarioClient.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().containsKey("id")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo obtener la información del usuario autenticado.");
        }

        // Construcción del pedido
        Pedido pedido = new Pedido();
        pedido.setIdEquipo((Integer) body.get("idEquipo"));
        pedido.setTotal(Double.parseDouble(body.get("total").toString()));
        pedido.setIdUsuario(idUsuario); // usar id real

        // Tipo de pedido
        Integer idTipo = (Integer) body.get("idTipo");

        // Cambiar a Map<String,Object> porque ahora el servicio devuelve un map con pedido+estado
        Map<String, Object> nuevoPedidoConEstado = pedidoService.crearPedido(pedido, idTipo);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedidoConEstado); // 201 Created

    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
    }

    // buscar pedido por id
    @Operation(summary = "Buscar un pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pedido.class)))),
            @ApiResponse(responseCode = "401", description = "Acceso denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idPedido}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable Integer idPedido) {
        try {
            Map<String, Object> pedidoCompleto = pedidoService.obtenerPedidoPorId(idPedido);
            return ResponseEntity.ok(pedidoCompleto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @Operation(summary = "Mostrar todos los pedidos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pedido.class)))),
            @ApiResponse(responseCode = "401", description = "Acceso denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Rol inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> mostrarTodosLosPedidos(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            List<Pedido> pedidos = pedidoService.mostrarTodosLosPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // eliminar pedido por id
    @Operation(summary = "Eliminar un pedido por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Rol inválido"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{idPedido}")
    public ResponseEntity<?> eliminarPedidoPorId(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idPedido) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            pedidoService.eliminarPedidoPorId(idPedido);
            return ResponseEntity.noContent().build(); // 204
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
    @Operation(summary = "Modificar un pedido existente")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pedido modificado exitosamente", content = @Content(schema = @Schema(implementation = Pedido.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
    @ApiResponse(responseCode = "401", description = "Acceso denegado: usuario no conectado"),
    @ApiResponse(responseCode = "403", description = "Acceso denegado: rol inválido"),
    @ApiResponse(responseCode = "404", description = "Pedido no encontrado o error en modificación"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
    @PutMapping("/modificar/{idPedido}")
    public ResponseEntity<?> modificarPedido(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idPedido,
            @RequestBody Map<String, Object> body) {
        try {
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRol(idUserConectado, 3);
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            Optional<Map<String, Object>> conectadoOpt = usuarioConectadoClient
                    .buscarUsuarioConectadoPorId(idUserConectado);
            if (conectadoOpt.isEmpty() || !conectadoOpt.get().containsKey("userId")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no conectado.");
            }
            Integer idUsuario = (Integer) conectadoOpt.get().get("userId");

            // Validar datos requeridos
            if (!body.containsKey("idEquipo") || !body.containsKey("total") || !body.containsKey("idTipo")) {
                return ResponseEntity.badRequest().body("Faltan datos obligatorios para modificar el pedido.");
            }

            // Construir objeto con los datos actualizados
            Pedido pedidoActualizado = new Pedido();
            pedidoActualizado.setIdUsuario(idUsuario); // usuario real
            pedidoActualizado.setIdEquipo((Integer) body.get("idEquipo"));
            pedidoActualizado.setIdEstado((Integer) body.getOrDefault("idEstado", 1)); // puede venir o no
            pedidoActualizado.setTotal(Double.parseDouble(body.get("total").toString()));

            Integer idTipo = (Integer) body.get("idTipo");

            Pedido pedidoModificado = pedidoService.modificarPedido(idPedido, pedidoActualizado, idTipo);

            return ResponseEntity.ok(pedidoModificado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al modificar el pedido");
        }
    }

}
