package com.example.GestorInventario.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.example.GestorInventario.model.Equipo;
import com.example.GestorInventario.model.Estado;
import com.example.GestorInventario.repository.EquipoRepository;
import com.example.GestorInventario.repository.EstadoRepository;
import com.example.GestorInventario.webclient.MarcaClient;
import com.example.GestorInventario.webclient.ModeloClient;

@Component
public class LoadDatabase {
        @Bean
    CommandLineRunner initDatabase(EquipoRepository equipoRepo, EstadoRepository estadoRepo,
                                    MarcaClient marcaClient, ModeloClient modeloClient) {
        return args -> {

            if (equipoRepo.count() == 0 && estadoRepo.count() == 0) {

                // cargar estados
                Estado disponible = new Estado(null, "Disponible");
                Estado arrendado = new Estado(null, "Arrendado");
                Estado vendido = new Estado(null, "Vendido");
                Estado enMantenimiento = new Estado(null, "En mantenimiento");
                Estado enRevision = new Estado(null, "En revisión");
                Estado danado = new Estado(null, "Dañado");
                Estado enTransito = new Estado(null, "En tránsito");
                Estado pendienteEntrega = new Estado(null, "Pendiente de entrega");
                Estado pendienteRecoleccion = new Estado(null, "Pendiente de recolección");
                
                // estados de Factura
                Estado Pendiente = new Estado(null, "Pendiente");
                Estado Pagada = new Estado(null, "Pagada");
                Estado Parcial = new Estado(null, "Parcial");
                //estados de Pago
                Estado Completado = new Estado(null, "Completado");
                Estado Fallido = new Estado(null, "Fallido");
                Estado EnProceso = new Estado(null, "En proceso");

                estadoRepo.saveAll(List.of(disponible, arrendado, vendido, enMantenimiento, enRevision,
                        danado, enTransito, pendienteEntrega, pendienteRecoleccion, Pendiente, Pagada, Parcial,
                        Completado, Fallido, EnProceso));

                // obtener marcas y modelos como lista de Map
                List<Map<String, Object>> marcas = marcaClient.obtenerTodasLasMarcas();
                List<Map<String, Object>> modelos = modeloClient.obtenerTodosLosModelos();


                // usamos los id
                Map<String, Object> johnDeere = marcaClient.obtenerMarcaPorId(1);
                Map<String, Object> newHolland = marcaClient.obtenerMarcaPorId(2);
                Map<String, Object> caseIH = marcaClient.obtenerMarcaPorId(3);

                Map<String, Object> modelo5055E = modeloClient.obtenerModeloPorId(1);
                Map<String, Object> modeloS680 = modeloClient.obtenerModeloPorId(2);
                Map<String, Object> modeloT6050 = modeloClient.obtenerModeloPorId(3);
                Map<String, Object> modeloCR790 = modeloClient.obtenerModeloPorId(4);
                Map<String, Object> modeloPuma185 = modeloClient.obtenerModeloPorId(5);
                Map<String, Object> modelo1230 = modeloClient.obtenerModeloPorId(6);
                Map<String, Object> modeloR4045 = modeloClient.obtenerModeloPorId(7);

                Equipo tractorJD5055E = new Equipo("Tractor John Deere 5055E", 650000.0, 2500.0,
                        "JD5055E-A",
                        (Integer) modelo5055E.get("idModelo"), (Integer) johnDeere.get("idMarca"), disponible);

                Equipo cosechadoraJDS680 = new Equipo("Cosechadora John Deere S680", 1200000.0, 5000.0,
                        "JD680-C",
                        (Integer) modeloS680.get("idModelo"), (Integer) johnDeere.get("idMarca"), arrendado);

                Equipo tractorNHT6050 = new Equipo("Tractor New Holland T6050", 700000.0, 2700.0,
                        "NHT6050-A",
                        (Integer) modeloT6050.get("idModelo"), (Integer) newHolland.get("idMarca"), enMantenimiento);

                Equipo cosechadoraNHCR790 = new Equipo("Cosechadora New Holland CR7.90", 1100000.0,
                        4800.0, "NHCR790-B",
                        (Integer) modeloCR790.get("idModelo"), (Integer) newHolland.get("idMarca"), disponible);

                Equipo tractorCasePuma185 = new Equipo("Tractor Case IH Puma 185", 780000.0, 2900.0,
                        "CHPUMA185-A",
                        (Integer) modeloPuma185.get("idModelo"), (Integer) caseIH.get("idMarca"), disponible);

                Equipo sembradoraCase1230 = new Equipo("Sembradora Case IH 1230", 950000.0, 4300.0,
                        "CH1230-S",
                        (Integer) modelo1230.get("idModelo"), (Integer) caseIH.get("idMarca"), disponible);

                Equipo pulverizadoraJDR4045 = new Equipo("Pulverizadora John Deere R4045", 600000.0,
                        2400.0, "JD4045-P",
                        (Integer) modeloR4045.get("idModelo"), (Integer) johnDeere.get("idMarca"), pendienteEntrega);

                equipoRepo.saveAll(List.of(
                        tractorJD5055E, cosechadoraJDS680, tractorNHT6050, cosechadoraNHCR790,
                        tractorCasePuma185, sembradoraCase1230, pulverizadoraJDR4045));

                System.out.println("Equipos precargados correctamente");
            } else {
                System.out.println("Ya existen datos en la base. No se cargó nada nuevo.");
            }

        };
    }
}
