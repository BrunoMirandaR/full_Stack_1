package com.example.PagoFactura.controller;

import java.util.List;
import java.util.Map;
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

import com.example.PagoFactura.Model.Factura;
import com.example.PagoFactura.service.AutorizacionService;
import com.example.PagoFactura.service.FacturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {
    @Autowired
    private FacturaService facturaService;
    @Autowired
    private AutorizacionService autorizacionService;

    @Operation(
        summary = "Generar una nueva factura",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Debe contener el idPedido del cual se generará la factura",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Ejemplo de solicitud",
                    value = "{\"idPedido\": 1}"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Factura generada exitosamente", content = @Content(schema = @Schema(implementation = Factura.class)))
    })
    @PostMapping("/generar")
    public ResponseEntity<?> generarFacturaDesdePedido(@RequestBody Map<String, Integer> body) {

        Integer idPedido = body.get("idPedido");
        if (idPedido == null) {
            return ResponseEntity.badRequest()
                    .body("El idPedido es obligatorio para generar la factura.");
        }
        try {
            Factura nuevaFactura = facturaService.generarFacturaDesdePedido(idPedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Otros errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al generar factura: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener una factura por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada", content = @Content(schema = @Schema(implementation = Factura.class))),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerFacturaPorId(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer id) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            return ResponseEntity.ok(facturaService.obtenerFacturaPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Listar todas las facturas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida correctamente", content = @Content(schema = @Schema(implementation = Factura.class))),
            @ApiResponse(responseCode = "204", description = "No hay facturas disponibles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<?> listarFacturas(@RequestHeader("X-User-Id") Integer idUserConectado) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            List<Factura> facturas = facturaService.listarFacturas();

            if (facturas.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }
            return ResponseEntity.ok(facturas); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las facturas: " + e.getMessage()); // 500
        }
    }

    @Operation(summary = "Eliminar una factura por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Factura eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarFactura(@RequestHeader("X-User-Id") Integer idUserConectado,
            @PathVariable Integer id) {
        try {
            // Validar rol del usuario conectado
            ResponseEntity<?> autorizacionResponse = autorizacionService.validarRoles(idUserConectado, Set.of(3, 6));
            if (!autorizacionResponse.getStatusCode().is2xxSuccessful()) {
                return autorizacionResponse;
            }
            facturaService.eliminarFactura(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
