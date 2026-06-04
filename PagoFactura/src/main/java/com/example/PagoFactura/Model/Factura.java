package com.example.PagoFactura.Model;

import java.time.LocalDate;

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
@Table(name = "factura")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de la factura", example = """
        {
      "idFactura": 1,
      "idPedido": 123,
      "fechaEmision": "2024-06-23",
      "montoTotal": 15000.0,
      "estado": "Pendiente"
    }
        """)
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental de la factura", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idFactura;

    @Schema(description = "ID del pedido asociado", required = true)
    @Column(nullable = false)
    private Integer idPedido;

    @Schema(description = "Fecha de emisión de la factura")
    private LocalDate fechaEmision;

    @Schema(description = "Monto total de la factura", required = true)
    @Column(nullable = false)
    private Double montoTotal;

    @Schema(description = "Estado de la factura (Pendiente, Pagada, Parcial)", example = "Pendiente")
    @Column(nullable = false, length = 20)
    private String estado;
}
