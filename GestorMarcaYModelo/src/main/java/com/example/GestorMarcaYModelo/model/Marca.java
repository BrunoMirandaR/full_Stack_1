package com.example.GestorMarcaYModelo.model;


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
@Table(name = "marca")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    description = "Datos de la marca",
    example = """
        {
          "idMarca": 2,
          "nombre": "John Deere"
        }
    """
)
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental de la marca", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idMarca;
    @Schema(description = "Nombre de la marca")
    @Column(nullable = false)
    private String nombre;

   
}
