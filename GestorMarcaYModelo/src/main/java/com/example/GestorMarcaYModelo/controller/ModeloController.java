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

import com.example.GestorMarcaYModelo.model.Modelo;
import com.example.GestorMarcaYModelo.service.ModeloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/modelos")
public class ModeloController {
    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    // muestra un modelo por id
    @Operation(summary = "Obtener un modelo por ID")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Modelo encontrado", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse (responseCode = "404", description = "Modelo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> obtenerModeloPorId(@PathVariable Integer id) {
        Modelo modelo = modeloService.obtenerModeloPorId(id);
        if (modelo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(modelo);
    }

    // muestra todos los modelos
    @Operation(summary = "Obtener una lista de todos los modelos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de modelos obtenida correctamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping()
    public ResponseEntity<List<Modelo>> listarModelos() {
        try {
            return ResponseEntity.ok(modeloService.listarModelos()); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 internal server error
        }
    }
  
    // crea un modelo
    @Operation(summary = "Guardar un nuevo modelo enlazado a una marca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Modelo creado correctamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/guardar")
    public ResponseEntity<Modelo> guardarModelo(@RequestBody Modelo modelo) {
        try {
        Modelo creado = modeloService.guardarModelo(modelo);
        if (creado == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(creado); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
        
    }

    // modificar un modelo
    @Operation(summary = "Modificar un modelo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Modelo modificado correctamente", content = @Content(schema = @Schema(implementation = Modelo.class))),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta, datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Modelo> modificarModelo(@PathVariable Integer id, @RequestBody Modelo modelo) {
        try {
        Modelo modeloExistente = modeloService.obtenerModeloPorId(id);
        if (modeloExistente == null) {
            return ResponseEntity.notFound().build();
        }

        modelo.setIdModelo(id);
        Modelo actualizado = modeloService.guardarModelo(modelo);
        return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    
    // eliminar un modelo por id
    @Operation(summary = "Eliminar un modelo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Modelo eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarModelo(@PathVariable Integer id) {
        try {
            modeloService.eliminarModelo(id); // Verifica si el modelo existe
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
        
        
    }
}
