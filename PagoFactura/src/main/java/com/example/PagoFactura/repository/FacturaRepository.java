package com.example.PagoFactura.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PagoFactura.Model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

}
