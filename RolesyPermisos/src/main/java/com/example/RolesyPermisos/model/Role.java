package com.example.RolesyPermisos.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@Table(name = "Rol")
@NoArgsConstructor
@Schema(description = "Modelo que representa datos de un rol en el sistema.")
public class Role {
    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del rol")
    private Integer idRole;

    // Nombre del rol
    @Column(nullable = false, length = 50)
    @Schema(description = "Nombre del rol")
    private String nombre;

    // Set de permisos que tiene este rol
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permiso", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    @Schema(description = "Permisos asociados a este rol")
    private List<Permiso> permisos = new ArrayList<>();

}
