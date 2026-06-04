package com.example.GestorInventario.model;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del equipo", example = """
            {
              "nombre": "Tractor John Deere X350",
              "precioVenta": 5500000,
              "precioArriendo": 150000,
              "patente": "TRC-350JD",
              "idModelo": 5,
              "idMarca": 2,
              "estado": {
                "idEstado": 1
              }
            }
        """)
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del equipo", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEquipo;

    @Column(nullable = false, length = 60)
    @Schema(description = "Nombre del equipo")
    private String nombre;

    @Column(nullable = false, length = 40)
    @Schema(description = "Precio de venta del equipo")
    private Double precioVenta;

    @Column(nullable = false, length = 40)
    @Schema(description = "Precio de arriendo del equipo")
    private Double precioArriendo;

    @Column(nullable = false, length = 40)
    @Schema(description = "Patente del equipo")
    private String patente;
    @Schema(description = "ID del modelo asociado al equipo", nullable = false)
    private Integer idModelo;
    @Schema(description = "ID de la marca asociada al equipo", nullable = false)
    private Integer idMarca;

    // variables para devolver el objeto completo
    @Transient
    @Schema(description = "Nombre del modelo asociado al equipo")
    private String modelo;

    @Transient
    @Schema(description = "Nombre de la marca asociada al equipo")
    private String marca;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    @Schema(description = "Nombre del estado del equipo")
    private Estado estado;

    // constructor para precargar un equipo sin contar la id
    public Equipo(String nombre, Double precioVenta, Double precioArriendo, String patente, Integer idModelo,
            Integer idMarca, Estado estado) {
        this.nombre = nombre;
        this.precioVenta = precioVenta;
        this.precioArriendo = precioArriendo;
        this.patente = patente;
        this.idModelo = idModelo;
        this.idMarca = idMarca;
        this.estado = estado;
    }
}
