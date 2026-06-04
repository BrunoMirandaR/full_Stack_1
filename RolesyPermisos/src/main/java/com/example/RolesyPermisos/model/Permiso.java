package com.example.RolesyPermisos.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Permiso")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa datos de un permiso en el sistema.")
public class Permiso {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del permiso")
    private Integer idPermiso;

    // Nombre del permiso
    @Schema(description = "Nombre del permiso")
    @Column(nullable = false, length = 50)
    private String nombre;

    // Set de roles que tienen este permiso (se ignora para evitar ciclos JSON)
    @ManyToMany(mappedBy = "permisos")
    @JsonIgnore
    @Schema(description = "Roles asociados a este permiso")
    private Set<Role> roles = new HashSet<>();

    // Constructor con nombre
    public Permiso(String nombre) {
        this.nombre = nombre;
    }
}
