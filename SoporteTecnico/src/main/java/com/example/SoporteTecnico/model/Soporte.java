package com.example.SoporteTecnico.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa un registro de soporte técnico asociado a un ticket.
 */
@Entity
@Table(name = "SOPORTE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un soporte técnico realizado para un ticket")
public class Soporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del soporte", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = true, length = 100)
    @Schema(description = "Observaciones o comentarios del soporte", example = "Revisión y ajuste realizado")
    private String observacion;

    @Column(nullable = false)
    @Schema(description = "Fecha en que se realizó el soporte", example = "2025-06-23T15:30:00Z", required = true)
    private Date fecha_soporte;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @Schema(description = "Ticket al cual está asociado este soporte")
    private Ticket ticket;

}
