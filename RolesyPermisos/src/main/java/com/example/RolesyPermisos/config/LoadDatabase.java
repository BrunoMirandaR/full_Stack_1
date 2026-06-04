package com.example.RolesyPermisos.config;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.RolesyPermisos.repository.PermisoRepository;
import com.example.RolesyPermisos.repository.RoleRepository;
import com.example.RolesyPermisos.model.Permiso;
import com.example.RolesyPermisos.model.Role;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepo, PermisoRepository permisoRepo) {
        return args -> {
            if (roleRepo.count() == 0 && permisoRepo.count() == 0) {

                // Borrar datos existentes para empezar desde cero
                roleRepo.deleteAll();
                permisoRepo.deleteAll();

                // ===== CREAR PERMISOS =====

                Permiso gestionarUsuarios = crearPermiso("Gestionar Usuarios");
                Permiso configurarPermisos = crearPermiso("Configurar Permisos");
                Permiso monitorizacionSistema = crearPermiso("Monitorización del Sistema");
                Permiso respaldarRestaurarDatos = crearPermiso("Respaldar y Restaurar Datos");

                Permiso administrarEquipos = crearPermiso("Administrar Equipos");
                Permiso controlStock = crearPermiso("Control de Stock");
                Permiso gestionProveedores = crearPermiso("Gestión de Proveedores");
                Permiso generacionReportes = crearPermiso("Generación de Reportes");

                Permiso gestionPedidos = crearPermiso("Gestión de Pedidos");
                Permiso organizacionEntregas = crearPermiso("Organización de Entregas");
                Permiso planificacionMantenimiento = crearPermiso("Planificación de Mantenimiento");
                Permiso controlDevoluciones = crearPermiso("Control de Devoluciones");

                Permiso registroIncidencias = crearPermiso("Registro de Incidencias");
                Permiso asignacionTecnicos = crearPermiso("Asignación de Técnicos");
                Permiso monitoreoSistema = crearPermiso("Monitoreo del Sistema");

                Permiso registroClientes = crearPermiso("Registro de Clientes");
                Permiso busquedaEquipos = crearPermiso("Búsqueda de Equipos");
                Permiso solicitudArriendoCompra = crearPermiso("Solicitud de Arriendo/Compra");
                Permiso seguimientoPedidos = crearPermiso("Seguimiento de Pedidos");
                Permiso gestionPerfil = crearPermiso("Gestión de Perfil");
                Permiso solicitudSoporteTecnico = crearPermiso("Solicitud de Soporte Técnico");
                Permiso dejarResenasCalificaciones = crearPermiso("Dejar Reseñas y Calificaciones");

                Permiso gestionFacturas = crearPermiso("Gestión de Facturas");
                Permiso registroPagos = crearPermiso("Registro de Pagos");
                Permiso consultaPagos = crearPermiso("Consulta de Pagos");
                Permiso actualizacionEstadosFactura = crearPermiso("Actualización de Estados de Factura");

                // Guardar todos los permisos
                List<Permiso> todosLosPermisos = Arrays.asList(
                        gestionarUsuarios, configurarPermisos, monitorizacionSistema, respaldarRestaurarDatos,
                        administrarEquipos, controlStock, gestionProveedores, generacionReportes,
                        gestionPedidos, organizacionEntregas, planificacionMantenimiento, controlDevoluciones,
                        registroIncidencias, asignacionTecnicos, monitoreoSistema,
                        registroClientes, busquedaEquipos, solicitudArriendoCompra, seguimientoPedidos,
                        gestionPerfil, solicitudSoporteTecnico, dejarResenasCalificaciones, gestionFacturas,
                        registroPagos, consultaPagos, actualizacionEstadosFactura);

                permisoRepo.saveAll(todosLosPermisos);

                // ===== CREAR ROLES CON SUS PERMISOS =====

                Role administrador = crearRoleConPermisos("Administrador", Arrays.asList(
                        gestionarUsuarios, configurarPermisos, monitorizacionSistema, respaldarRestaurarDatos));

                Role gestorInventario = crearRoleConPermisos("Gestor de Inventario", Arrays.asList(
                        administrarEquipos, controlStock, gestionProveedores, generacionReportes));

                Role coordinadorLogistico = crearRoleConPermisos("Coordinador Logístico", Arrays.asList(
                        gestionPedidos, organizacionEntregas, planificacionMantenimiento, controlDevoluciones));

                Role soporteTecnico = crearRoleConPermisos("Soporte Técnico", Arrays.asList(
                        registroIncidencias, asignacionTecnicos, monitoreoSistema));

                Role cliente = crearRoleConPermisos("Cliente", Arrays.asList(
                        registroClientes, busquedaEquipos, solicitudArriendoCompra, seguimientoPedidos,
                        gestionPerfil, solicitudSoporteTecnico, dejarResenasCalificaciones));
                Role finanzas = crearRoleConPermisos("Finanzas", Arrays.asList(
                        gestionFacturas, registroPagos, consultaPagos, actualizacionEstadosFactura));
                // Guardar todos los roles
                roleRepo.saveAll(Arrays.asList(
                        administrador, gestorInventario, coordinadorLogistico, soporteTecnico, cliente, finanzas));

                System.out.println("Datos iniciales cargados exitosamente:");
                System.out.println(roleRepo.count() + "- roles creados");
                System.out.println(permisoRepo.count() + "- permisos creados");
                System.out.println("- Permisos asignados a roles correspondientes");

            } else {
                System.out.println("Datos ya existentes. No se cargaron nuevos datos");
            }
        };
    }

    // Método auxiliar para crear permisos
    private Permiso crearPermiso(String nombre) {
        Permiso permiso = new Permiso();
        permiso.setNombre(nombre);
        return permiso;
    }

    // Método auxiliar para crear roles con permisos
    private Role crearRoleConPermisos(String nombre, List<Permiso> permisos) {
        Role role = new Role();
        role.setNombre(nombre);
        role.setPermisos(new ArrayList<>(permisos)); // Se usa ArrayList en lugar de HashSet
        return role;
    }
}
