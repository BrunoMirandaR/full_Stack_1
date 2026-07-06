package com.example.PagoFactura.config;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class LoadDatabaseTest {

    @Test
    void constructor_shouldLoadInitialStates() {
        LoadDatabase loadDatabase = new LoadDatabase();

        assertThat(loadDatabase.obtenerNombreEstadoFactura(10)).isEqualTo("Pendiente");
        assertThat(loadDatabase.obtenerNombreEstadoFactura(11)).isEqualTo("Pagada");
        assertThat(loadDatabase.obtenerNombreEstadoFactura(12)).isEqualTo("Parcial");
        assertThat(loadDatabase.obtenerNombreEstadoPago(13)).isEqualTo("Completado");
        assertThat(loadDatabase.obtenerNombreEstadoPago(14)).isEqualTo("Fallido");
        assertThat(loadDatabase.obtenerNombreEstadoPago(15)).isEqualTo("En proceso");
    }

    @Test
    void mapsShouldBeUnmodifiable() {
        LoadDatabase loadDatabase = new LoadDatabase();

        assertThat(loadDatabase.getEstadosFactura()).containsEntry(10, "Pendiente");
        assertThat(loadDatabase.getEstadosPago()).containsEntry(13, "Completado");
    }
}