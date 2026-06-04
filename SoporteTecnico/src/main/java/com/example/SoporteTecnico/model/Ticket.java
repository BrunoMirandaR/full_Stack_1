package com.example.SoporteTecnico.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa un ticket de soporte técnico con su información básica y sus soportes asociados.
 */
@Entity
@Table(name = "TICKET")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un ticket de soporte técnico")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del ticket", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Fecha de inicio del ticket", example = "2025-06-23T09:30:00Z", required = true)
    private Date fecha_inicio;

    @Column(nullable = true)
    @Schema(description = "Fecha de cierre del ticket", example = "2025-06-24T15:45:00Z")
    private Date fecha_cierre;

    @Column(nullable = true)
    @Schema(description = "Descripción del problema o solicitud", example = "No funciona la impresora")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que creó el ticket", example = "5", required = true)
    private Integer idUsuario;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(description = "Lista de registros de soporte asociados al ticket", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Soporte> soportes;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(description = "Lista de tipos de soporte asociados al ticket", accessMode = Schema.AccessMode.READ_ONLY)
    private List<TipoSoporte> tiposSportes; // Nota: probablemente 'tiposSoportes' es el nombre correcto

}
