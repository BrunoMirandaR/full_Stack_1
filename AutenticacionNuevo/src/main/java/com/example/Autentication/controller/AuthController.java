package com.example.Autentication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Autentication.auth.AuthResponse;
import com.example.Autentication.auth.LoginRequest;
import com.example.Autentication.model.UsuarioConectado;
import com.example.Autentication.service.AuthService;
import com.example.Autentication.service.UsuarioConectadoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth") // Agrupa todas las rutas bajo /auth
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación y usuarios conectados")
public class AuthController {

    private final AuthService authService;

    @Autowired
    private UsuarioConectadoService usuarioConectadoService;

    @Operation(summary = "Autentica un usuario y retorna el token de acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Datos para autenticar usuario", required = true)
            @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error: " + ex.getMessage());
        }
    }

    @Operation(summary = "Muestra todos los usuarios conectados actualmente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = UsuarioConectado.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/conectados")
    public ResponseEntity<List<UsuarioConectado>> mostrarConectados() {
        try {
            List<UsuarioConectado> lista = usuarioConectadoService.ListarUsuariosConectados();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtiene un usuario conectado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario conectado encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioConectado.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioConectado> obtenerPorId(
            @Parameter(description = "ID del usuario conectado", required = true)
            @PathVariable Integer id) {
        UsuarioConectado conectado = usuarioConectadoService.obtenerUsuarioConectadoPorId(id);
        if (conectado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(conectado);
    }

}
