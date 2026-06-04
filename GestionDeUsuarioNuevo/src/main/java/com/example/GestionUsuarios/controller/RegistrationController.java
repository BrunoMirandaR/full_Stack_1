package com.example.GestionUsuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.GestionUsuarios.auth.AuthResponse;
import com.example.GestionUsuarios.auth.RegisterRequest;
import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.service.RegistrationService;
import com.example.GestionUsuarios.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Operaciones para registrar, actualizar, eliminar y consultar usuarios")
public class RegistrationController {
    
    private final RegistrationService registrationService;
    private final UserService userService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida (falta username o password)",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @Parameter(description = "Datos del nuevo usuario para registro", required = true)
        @RequestBody RegisterRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Error: El nombre de usuario y la contraseña son obligatorios."));
        }

        AuthResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Obtener usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido (nulo o menor que 1)",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Usuario no encontrado",
                     content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
        @Parameter(description = "ID del usuario a buscar", required = true, example = "1")
        @PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID debe ser un número positivo.");
        }

        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userOptional.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: Usuario no encontrado.");
        }
    }

    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido (menor que 1)",
                     content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
        @PathVariable Integer id,
        @Parameter(description = "Datos actualizados del usuario", required = true)
        @RequestBody User updatedUser) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID debe ser un número positivo.");
        }

        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "400", description = "ID inválido (menor que 1)",
                     content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
        @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
        @PathVariable Integer id) {
        if (id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: El ID debe ser un número positivo.");
        }

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado correctamente.");
    }

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Error al obtener usuarios",
                     content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/usuarios")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.mostrarUsuarios();
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: No se pudieron obtener los usuarios.");
        }
    }

    @Operation(summary = "Obtener usuario por nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<?> obtenerPorUsername(
        @Parameter(description = "Nombre de usuario a buscar", required = true, example = "cgomez")
        @PathVariable String username) {
        try {
            User user = userService.obtenerPorUsername(username);
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario no encontrado con username: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ocurrió un error al buscar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
