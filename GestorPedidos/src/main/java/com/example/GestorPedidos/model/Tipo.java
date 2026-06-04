package com.example.GestorPedidos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un tipo de pedido. Por ejemplo: Compra, Alquiler, Reparación.")
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    @Schema(
        description = "ID autogenerado del tipo de pedido",
        example = "1"
    )
    private Integer idTipo;

    @Column(name = "nombre", nullable = false, length = 40)
    @Schema(
        description = "Nombre descriptivo del tipo de pedido",
        example = "Alquiler"
    )
    private String nombre;
}
