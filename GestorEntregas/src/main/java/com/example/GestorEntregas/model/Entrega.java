package com.example.GestorEntregas.model;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Table(name = "entrega")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una entrega asociada a un pedido.")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado de la entrega", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEntrega;

    @Column(name = "comentario")
    @Schema(description = "Comentario adicional sobre la entrega", example = "Cliente no estaba disponible.")
    private String comentario;

    @Column(name = "fecha_entrega", nullable = false)
    @Schema(description = "Fecha programada para la entrega", example = "2025-06-18")
    private LocalDate fechaEntrega;

    @Schema(description = "ID del pedido asociado a la entrega", example = "501")
    private Integer idPedido;

    @Schema(description = "Dirección exacta donde se realizará la entrega", example = "Av. Siempre Viva 742")
    @Column(name = "direccion_entrega", nullable = false)
    private String direccionEntrega;

    @Schema(description = "Nombre de la comuna de la entrega", example = "Providencia")
    @Column(name = "comuna", nullable = false)
    private String comuna;

    @Schema(description = "Nombre de la ciudad de la entrega", example = "Santiago")
    @Column(name = "ciudad", nullable = false)
    private String ciudad;


    @Schema(description = "Estado actual de la entrega", example = "En camino")
    @Column(name = "estado", nullable = false)
    private String estado;

    @Schema(description = "Usuario que registró o modificó la entrega", example = "admin@empresa.cl")
    private String usuario;
}
