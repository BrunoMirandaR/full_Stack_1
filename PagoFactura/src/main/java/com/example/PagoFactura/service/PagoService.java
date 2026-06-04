package com.example.PagoFactura.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PagoFactura.Model.Factura;
import com.example.PagoFactura.Model.Pago;
import com.example.PagoFactura.repository.PagoRepository;

@Service
public class PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private FacturaService facturaService;

    /**
     * Registrar un nuevo pago asociado a una factura.
     */
    public Pago registrarPago(Pago pago) {
        // Validar que la factura exista
        Factura factura = facturaService.obtenerFacturaPorId(pago.getIdFactura());

        pago.setFechaPago(LocalDate.now());
        pago.setEstado("Completado"); // por ahora se asume éxito
        Pago pagoGuardado = pagoRepository.save(pago);

        // Verificar si el pago cubre el total de la factura
        Double totalPagado = pagoRepository.sumPagosPorFactura(pago.getIdFactura());
        if (totalPagado >= factura.getMontoTotal()) {
            factura.setEstado("Pagada");
        } else if (totalPagado > 0) {
            factura.setEstado("Parcial");
        } else {
            factura.setEstado("Pendiente");

        }
        facturaService.actualizarFactura(factura);
        return pagoGuardado;
    }

    // obtener pagos por factura
    public List<Pago> listarPagosPorFactura(Integer idFactura) {
        return pagoRepository.findByIdFactura(idFactura);
    }

    // eliminar un pago por su ID
    public void eliminarPago(Integer idPago) {
        if (!pagoRepository.existsById(idPago)) {
            throw new RuntimeException("Pago no encontrado");
        }
        pagoRepository.deleteById(idPago);
    }
}
