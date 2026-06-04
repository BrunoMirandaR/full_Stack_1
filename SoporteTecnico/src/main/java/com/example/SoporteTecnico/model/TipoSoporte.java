package com.example.SoporteTecnico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representa un tipo de soporte técnico asociado a un ticket.
 */
@Entity
@Table(name = "TIPOSOPORTE")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Entidad que representa un tipo de soporte técnico")
public class TipoSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del tipo de soporte", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Column(nullable = false, length = 10)
    @Schema(description = "Nombre del tipo de soporte", example = "Hardware", required = true)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @Schema(description = "Ticket asociado a este tipo de soporte")
    private Ticket ticket;
}
