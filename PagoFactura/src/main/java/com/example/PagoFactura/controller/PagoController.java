package com.example.PagoFactura.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PagoFactura.Model.Pago;
import com.example.PagoFactura.service.AutorizacionService;
import com.example.PagoFactura.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/pagos")

public class PagoController {
    @Autowired
    private PagoService pagoService;
    @Autowired
    private AutorizacionService autorizacionService;

    @Operation(summary = "Registrar un nuevo pago")
    @ApiResponse(responseCode = "201", description = "Pago registrado correctamente", content = @Content(schema = @Schema(implementation = Pago.class)))
    @ApiResponse(responseCode = "400", description = "Error de validación o datos incorrectos")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    @PostMapping
    public ResponseEntity<?> registrarPago(@RequestHeader("X-User-Id") Integer idUserConectado,
            @RequestBody Pago pago) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            Pago nuevo = pagoService.registrarPago(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el pago: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar pagos por id de factura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos obtenidos correctamente", content = @Content(schema = @Schema(implementation = Pago.class))),
            @ApiResponse(responseCode = "204", description = "No hay pagos para esta factura"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/factura/{idFactura}")
    public ResponseEntity<?> listarPagosPorFactura(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer idFactura) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            List<Pago> pagos = pagoService.listarPagosPorFactura(idFactura);
            if (pagos.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(pagos); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los pagos: " + e.getMessage()); // 500 Internal Server Error
        }
    }

    @Operation(summary = "Eliminar un pago por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPago(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer id) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }

            pagoService.eliminarPago(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
