package com.example.GestionUsuarios.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterRequest {

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido paterno del usuario", example = "Pérez")
    private String appaterno;

    @Schema(description = "Apellido materno del usuario", example = "Gómez", nullable = true)
    private String apmaterno;

    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombre de usuario para login", example = "juanperez")
    private String username;

    @Schema(description = "Contraseña para login", example = "P@ssw0rd123")
    private String password;

    @Schema(description = "ID del rol asignado al usuario", example = "2")
    private Integer idRol;
}
