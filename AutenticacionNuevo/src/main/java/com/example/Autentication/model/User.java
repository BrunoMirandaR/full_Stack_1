package com.example.Autentication.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Schema(description = "Entidad que representa a un usuario del sistema")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único del usuario", example = "1")
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Apellido paterno del usuario", example = "Gómez")
    private String appaterno;

    @Column(nullable = true)
    @Schema(description = "Apellido materno del usuario", example = "Martínez")
    private String apmaterno;

    @Column(nullable = false)
    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Column(nullable = false)
    @Schema(description = "Nombre de usuario para login", example = "cgomez")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario (encriptada)", example = "$2a$10$...")
    private String password;

    @Column(nullable = false)
    @Schema(description = "ID del rol asociado al usuario", example = "1")
    private String id_rol;

    // Métodos de UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
