package com.example.GestionUsuarios.model;

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

/**
 * Entidad que representa un usuario en el sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@Schema(description = "Entidad Usuario que representa los datos de un usuario del sistema")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Schema(description = "Identificador único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Apellido paterno del usuario", example = "Pérez")
    private String appaterno;

    @Column(nullable = true)
    @Schema(description = "Apellido materno del usuario", example = "González", nullable = true)
    private String apmaterno;

    @Column(nullable = false)    
    @Schema(description = "RUT del usuario", example = "12345678-9")
    private String rut;

    @Column(nullable = false)
    @Schema(description = "Nombre de usuario único para autenticación", example = "juanperez")
    private String username;

    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario", example = "********")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Identificador del rol asignado al usuario", example = "2")
    private Integer idRol;

    // Métodos sobrescritos y constructor omitidos para no repetir

    /**
     * Obtiene las autoridades concedidas al usuario.
     * En este caso, retorna una colección vacía porque no se usan roles con GrantedAuthority.
     * 
     * @return colección vacía de {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    
    /**
     * Indica si la cuenta del usuario no ha expirado.
     * 
     * @return true siempre (la cuenta nunca expira)
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * Indica si la cuenta del usuario no está bloqueada.
     * 
     * @return true siempre (la cuenta nunca está bloqueada)
     */
    @Override
    public boolean isAccountNonLocked()  { return true; }

    /**
     * Indica si las credenciales (contraseña) no han expirado.
     * 
     * @return true siempre (las credenciales nunca expiran)
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * Indica si el usuario está habilitado.
     * 
     * @return true siempre (el usuario siempre está habilitado)
     */
    @Override
    public boolean isEnabled() { return true; }

    /**
     * Constructor para precargar usuarios con id, username, password e idRol.
     * 
     * @param id Identificador del usuario
     * @param username Nombre de usuario
     * @param password Contraseña del usuario
     * @param idRol Identificador del rol asignado
     */
    public User(Integer id, String username, String password, Integer idRol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
    }
}
