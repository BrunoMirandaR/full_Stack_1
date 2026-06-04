package com.example.GestionUsuarios.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.GestionUsuarios.model.User;
import com.example.GestionUsuarios.repository.UserRepository;
import com.example.GestionUsuarios.webclient.RoleClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {
    private final UserRepository userRepository;
    private final RoleClient roleClient;

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleClient roleClient) {
        return args -> {
            if (userRepository.count() == 0) {
                // Obtener roles desde microservicio de roles
                Map<String, Object> adminRole = roleClient.obtenerRolPorNombreSinValidacion("Administrador");
                Map<String, Object> inventarioRole = roleClient.obtenerRolPorNombreSinValidacion("Gestor de Inventario" );
                Map<String, Object> logisticaRole = roleClient.obtenerRolPorNombreSinValidacion("Coordinador Logístico" );
                Map<String, Object> soporteRole = roleClient.obtenerRolPorNombreSinValidacion("Soporte Técnico" );
                Map<String, Object> clienteRole = roleClient.obtenerRolPorNombreSinValidacion("Cliente" );
                Map<String, Object> finanzasRole = roleClient.obtenerRolPorNombreSinValidacion("Finanzas");

                // ADMIN
                User admin = new User();
                admin.setNombre("Administrador");
                admin.setAppaterno("Sistema");
                admin.setApmaterno("Central");
                admin.setRut("11111111-1");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setIdRol(getIdFromRole(adminRole));

                // GESTOR DE INVENTARIO
                User gestorInv = new User();
                gestorInv.setNombre("Gestor");
                gestorInv.setAppaterno("Inventario");
                gestorInv.setApmaterno("Demo");
                gestorInv.setRut("22222222-2");
                gestorInv.setUsername("gestorInv");
                gestorInv.setPassword(passwordEncoder.encode("gestor123"));
                gestorInv.setIdRol(getIdFromRole(inventarioRole));

                // COORDINADOR LOGÍSTICO
                User coordLog = new User();
                coordLog.setNombre("Coordinador");
                coordLog.setAppaterno("Logístico");
                coordLog.setApmaterno("Demo");
                coordLog.setRut("33333333-3");
                coordLog.setUsername("coordLog");
                coordLog.setPassword(passwordEncoder.encode("coord123"));
                coordLog.setIdRol(getIdFromRole(logisticaRole));

                // SOPORTE TÉCNICO
                User soporte = new User();
                soporte.setNombre("Soporte");
                soporte.setAppaterno("Técnico");
                soporte.setApmaterno("Demo");
                soporte.setRut("44444444-4");
                soporte.setUsername("soporte");
                soporte.setPassword(passwordEncoder.encode("soporte123"));
                soporte.setIdRol(getIdFromRole(soporteRole));

                // CLIENTE 1
                User cliente1 = new User();
                cliente1.setNombre("Cliente");
                cliente1.setAppaterno("Usuario");
                cliente1.setApmaterno("Demo");
                cliente1.setRut("55555555-5");
                cliente1.setUsername("cliente1");
                cliente1.setPassword(passwordEncoder.encode("cliente123"));
                cliente1.setIdRol(getIdFromRole(clienteRole));

                // CLIENTE 2
                User cliente2 = new User();
                cliente2.setNombre("Cliente2");
                cliente2.setAppaterno("Usuario2");
                cliente2.setApmaterno("Demo");
                cliente2.setRut("66666666-6");
                cliente2.setUsername("cliente2");
                cliente2.setPassword(passwordEncoder.encode("cliente123"));
                cliente2.setIdRol(getIdFromRole(clienteRole));
                
                User finanzas = new User();
                finanzas.setNombre("Encargado");
                finanzas.setAppaterno("Finanzas");
                finanzas.setApmaterno("Sistema");
                finanzas.setRut("88888888-8");
                finanzas.setUsername("finanzas1");
                finanzas.setPassword(passwordEncoder.encode("finanzas123"));
                finanzas.setIdRol(getIdFromRole(finanzasRole));
                userRepository.saveAll(List.of(admin, gestorInv, coordLog, soporte, cliente1, cliente2, finanzas));
                System.out.println("Usuarios precargados con roles.");
            } else {
                System.out.println("Ya existen usuarios en la base, no se cargó nada nuevo.");
            }
        };
    }

    private Integer getIdFromRole(Map<String, Object> roleMap) {
        Object idObj = roleMap.get("idRole");
        if (idObj instanceof Integer) {
            return (Integer) idObj;
        } else if (idObj instanceof String) {
            return Integer.parseInt((String) idObj);
        } else {
            throw new IllegalArgumentException("ID de rol inválido: " + idObj);
        }
    }

}
