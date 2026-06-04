package com.example.GestorPedidos.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del pedido")
    private Integer idPedido;

    @Schema(description = "ID del usuario que realiza el pedido")
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Schema(description = "ID del equipo asociado al pedido")
    @Column(name = "id_equipo", nullable = false)
    private Integer idEquipo;
    
    @Schema(description = "Fecha del pedido")
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Schema(description = "ID del estado del pedido")
    @Column(name = "id_estado", nullable = false)
    private Integer idEstado;

    @Schema(description = "Costo total del pedido")
    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    @Schema(description = "Tipo de pedido ")
    private Tipo tipo;

}
