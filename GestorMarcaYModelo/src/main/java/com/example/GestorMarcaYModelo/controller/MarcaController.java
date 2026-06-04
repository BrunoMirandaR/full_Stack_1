package com.example.GestorMarcaYModelo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestorMarcaYModelo.model.Marca;
import com.example.GestorMarcaYModelo.service.MarcaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/marcas")
public class MarcaController {
    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    // mostrar todas las marcas
    @Operation(summary = "Obtener una lista de todas las marcas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de marcas obtenida correctamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("")
    public ResponseEntity<List<Marca>> listarMarcas() {
        try {
            return ResponseEntity.ok(marcaService.listarMarcas()); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 internal server error
        }
    }

    // obtener una marca por id
    @Operation(summary = "Obtener una marca por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca encontrada", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Marca> obtenerMarcaPorId(@PathVariable Integer id) {
        try {
            Marca marca = marcaService.obtenerMarcaPorId(id);
            if (marca == null) {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
            return ResponseEntity.ok(marca); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // crear una marca
    @Operation(summary = "Guardar una nueva marca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Marca creada correctamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/guardar")
    public ResponseEntity<Marca> guardarMarca(@RequestBody Marca marca) {
        try {
            Marca creada = marcaService.guardarMarca(marca);
            if (creada == null) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);// 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }

    }

    // modificar una marca por id
    @Operation(summary = "Modificar una marca existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Marca modificada correctamente", content = @Content(schema = @Schema(implementation = Marca.class))),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("modificar/{id}")
    public ResponseEntity<Marca> modificarMarca(@PathVariable Integer id, @RequestBody Marca marca) {
        try {
            Marca marcaExistente = marcaService.obtenerMarcaPorId(id);
            if (marcaExistente == null) {
                return ResponseEntity.notFound().build();// 404 Not Found
            }
            marca.setIdMarca(id);
            Marca actualizada = marcaService.guardarMarca(marca);
            return ResponseEntity.ok(actualizada); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // eliminar una marca por id
    @Operation(summary = "Eliminar una marca por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarMarca(@PathVariable Integer id) {
        try {
            marcaService.eliminarMarca(id);
            return ResponseEntity.noContent().build();// 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
