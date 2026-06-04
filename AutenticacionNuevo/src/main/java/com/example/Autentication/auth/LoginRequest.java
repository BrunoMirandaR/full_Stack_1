package com.example.Autentication.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para iniciar sesión en el sistema")
public class LoginRequest {

    @Schema(description = "Nombre de usuario para autenticación", example = "cgomez", required = true)
    private String username;

    @Schema(description = "Contraseña del usuario", example = "123456", required = true)
    private String password;
}
