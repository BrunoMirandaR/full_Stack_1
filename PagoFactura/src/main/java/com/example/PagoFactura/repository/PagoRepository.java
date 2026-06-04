package com.example.PagoFactura.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.PagoFactura.Model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByIdFactura(Integer idFactura);

    // Metodo para sumar los montos pagados por una factura
    @Query("SELECT SUM(p.montoPagado) FROM Pago p WHERE p.idFactura = :idFactura")
    Double sumPagosPorFactura(@Param("idFactura") Integer idFactura);
}
