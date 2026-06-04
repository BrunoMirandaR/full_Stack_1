package com.example.PagoFactura.config;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class LoadDatabase {
    private static final Map<Integer, String> estadosFactura = new ConcurrentHashMap<>();
    private static final Map<Integer, String> estadosPago = new ConcurrentHashMap<>();
    private static boolean cargado = false;

     public LoadDatabase() {
        if (!cargado) {
            synchronized (LoadDatabase.class) {
                if (!cargado) {
                    cargarEstados();
                    cargado = true;
                    System.out.println("Estados de factura y pago cargados correctamente.");
                } else {
                    System.out.println("Estados ya estaban cargados, no se recargan.");
                }
            }
        }
    }

    private void cargarEstados() {
        estadosFactura.put(10, "Pendiente");
        estadosFactura.put(11, "Pagada");
        estadosFactura.put(12, "Parcial");

        estadosPago.put(13, "Completado");
        estadosPago.put(14, "Fallido");
        estadosPago.put(15, "En proceso");
    }

    public String obtenerNombreEstadoFactura(Integer id) {
        return estadosFactura.get(id);
    }

    public String obtenerNombreEstadoPago(Integer id) {
        return estadosPago.get(id);
    }

    public Map<Integer, String> getEstadosFactura() {
        return Collections.unmodifiableMap(estadosFactura);
    }

    public Map<Integer, String> getEstadosPago() {
        return Collections.unmodifiableMap(estadosPago);
    }
}
