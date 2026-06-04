package com.example.Autentication.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario_conectado")
@Schema(description = "Representa un usuario actualmente conectado al sistema")
public class UsuarioConectado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de conexión", example = "1")
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "ID del usuario autenticado", example = "5")
    private Integer userId;

    @Column(nullable = false)
    @Schema(description = "Nombre de usuario conectado", example = "cgomez")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Token JWT asociado a la sesión activa", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private String token;
    
}