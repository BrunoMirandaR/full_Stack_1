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
@Table(name = "pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos del pago")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autoincremental del pago", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPago;

    @Schema(description = "ID de la factura asociada", required = true)
    @Column(nullable = false)
    private Integer idFactura;

    @Schema(description = "Monto pagado", required = true)
    @Column(nullable = false)
    private Double montoPagado;

    @Schema(description = "Fecha del pago")
    private LocalDate fechaPago;

    @Schema(description = "Medio de pago (Efectivo, Transferencia, etc.)", example = "Transferencia")
    @Column(nullable = false, length = 30)
    private String medioPago;

    @Schema(description = "Estado del pago (Completado, Fallido)", example = "Completado")
    @Column(nullable = false, length = 20)
    private String estado;
}
