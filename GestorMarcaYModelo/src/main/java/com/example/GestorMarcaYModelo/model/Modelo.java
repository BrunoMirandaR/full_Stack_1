package com.example.GestorMarcaYModelo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "modelo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    description = "Datos del modelo",
    example = """
        {
          "idModelo": 5,
          "nombre": "X350",
          "idMarca": 2
        }
    """
)
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del modelo", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idModelo;

    @Schema(description = "Nombre del modelo")
    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    @JsonIgnoreProperties("modelos") // evita la recursividad infinita
    @Schema(description = "Marca asociada al modelo")
    private Marca marca;

    // JSON incluya el idMarca
    @JsonProperty("idMarca")
    @Schema(description = "Comprobacion del id de la marca")
    public Integer getIdMarca() {
        return marca != null ? marca.getIdMarca() : null;
    }

    @JsonProperty("idMarca")
    @Schema(description = "Establece el id de la marca")
    public void setIdMarca(Integer idMarca) {
        if (idMarca != null) {
            this.marca = new Marca();
            this.marca.setIdMarca(idMarca);
        }
    }
}
