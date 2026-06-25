#  AgroTech - Sistema de Gestión Agrícola

##  Descripción del Proyecto

**AgroTech** es una plataforma integral de gestión agrícola basada en arquitectura de **microservicios con Spring Boot 3.5.3**. El sistema permite la gestión completa de operaciones agrícolas incluyendo usuarios, inventario de equipos, pedidos, entregas, facturas y soporte técnico.

### Características Principales:
-  Arquitectura de microservicios escalable
-  Autenticación y autorización con JWT
-  Control de roles y permisos
-  Gestión de inventario de equipos agrícolas
-  Sistema de pedidos y entregas
-  Gestión de pagos y facturas
-  Sistema de soporte técnico
-  Integración inter-servicios con WebClient
-  Documentación automática con Swagger/OpenAPI
-  Persistencia con JPA/Hibernate
-  Base de datos MySQL 8

---

##  Equipo de Desarrollo

| Nombre | Rol | Contacto |
|--------|-----|----------|
| [Bruno Miranda] | Full Stack Developer | br.mirandar@duocuc.cl |


*Proyecto desarrollado como parte del curso Full Stack 1 - DUOC UC*

---

##  Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway / Load Balancer                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
    ┌───▼────┐         ┌───▼────┐        ┌───▼────┐
    │ Auth   │         │ Roles  │        │Usuarios│
    │Service │         │Service │        │Service │
    │ 8082   │         │ 8089   │        │ 8081   │
    └────────┘         └────────┘        └────────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
    ┌───▼────┐         ┌───▼────┐        ┌───▼────┐
    │Inventory│        │ Orders │        │Delivery│
    │Service  │        │Service │        │Service │
    │ 8083    │        │ 8085   │        │ 8088   │
    └────────┘        └────────┘        └────────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
    ┌───▼────┐         ┌───▼────┐        ┌───▼────┐
    │ Brands │         │Payments│        │ Support│
    │ Models │         │Invoices│        │Service │
    │ 8086   │         │ 8087   │        │ 8084   │
    └────────┘         └────────┘        └────────┘
                           │
        ┌──────────────────┴──────────────────┐
        │                                     │
    ┌───▼─────────────┐          ┌───────────▼─────┐
    │ MySQL Database  │          │ Swagger/OpenAPI │
    │   agrotech_db   │          │  Documentation  │
    └─────────────────┘          └─────────────────┘
```

---

##  Microservicios

### 1. **AutenticacionNuevo** (Puerto 8082)
Servicio de autenticación y gestión de sesiones.

**Funcionalidades:**
- Login de usuarios con validación
- Generación de tokens JWT
- Gestión de usuarios conectados
- Logout

**Endpoints:**
- `POST /auth/login` - Login
- `GET /auth/conectados` - Obtener usuarios conectados

---

### 2. **GestionDeUsuarioNuevo** (Puerto 8081)
Gestión de usuarios del sistema.

**Funcionalidades:**
- CRUD completo de usuarios
- Registro de nuevos usuarios
- Asignación de roles
- Validación de credenciales

**Endpoints:**
- `GET /api/v1/usuarios` - Obtener todos
- `GET /api/v1/usuarios/{id}` - Obtener por ID
- `POST /api/v1/usuarios/register` - Registrar usuario
- `PUT /api/v1/usuarios/{id}` - Actualizar
- `DELETE /api/v1/usuarios/{id}` - Eliminar

---

### 3. **RolesyPermisos** (Puerto 8089)
Gestión de roles y permisos del sistema.

**Funcionalidades:**
- CRUD de roles
- CRUD de permisos
- Asociación roles-permisos (ManyToMany)
- Control granular de acceso

**Roles Predefinidos:**
- Administrador
- Gestor de Inventario
- Coordinador Logístico
- Soporte Técnico
- Cliente
- Finanzas

**Endpoints:**
- `GET /api/v1/roles` - Obtener todos
- `POST /api/v1/roles` - Crear
- `PUT /api/v1/roles/{id}` - Actualizar
- `DELETE /api/v1/roles/{id}` - Eliminar
- `GET /api/v1/permisos` - Obtener permisos

---

### 4. **GestorMarcaYModelo** (Puerto 8086)
Gestión de marcas y modelos de equipos.

**Funcionalidades:**
- CRUD de marcas (John Deere, Case IH, AGCO, etc.)
- CRUD de modelos
- Relaciones ManyToOne entre modelos y marcas
- Catálogo de equipos agrícolas

**Endpoints:**
- `GET /api/v1/marcas` - Obtener marcas
- `POST /api/v1/marcas` - Crear marca
- `PUT /api/v1/marcas/{id}` - Actualizar marca
- `DELETE /api/v1/marcas/{id}` - Eliminar marca
- `GET /api/v1/modelos` - Obtener modelos
- `POST /api/v1/modelos` - Crear modelo
- `PUT /api/v1/modelos/{id}` - Actualizar modelo
- `DELETE /api/v1/modelos/{id}` - Eliminar modelo

---

### 5. **GestorInventario** (Puerto 8083)
Gestión del inventario de equipos.

**Funcionalidades:**
- CRUD de equipos
- Gestión de estados de equipos (Disponible, En Uso, Mantenimiento, Dañado)
- Número de serie único por equipo
- Integración con GestorMarcaYModelo
- Filtrado por estado

**Endpoints:**
- `GET /api/v1/equipos` - Obtener todos
- `GET /api/v1/equipos/{id}` - Obtener por ID
- `POST /api/v1/equipos/guardar` - Crear
- `PUT /api/v1/equipos/modificar/{id}` - Actualizar
- `DELETE /api/v1/equipos/eliminar/{id}` - Eliminar
- `GET /api/v1/equipos/estado/{nombre}` - Obtener por estado
- `GET /api/v1/equipos/estados` - Obtener todos los estados

---

### 6. **GestorPedidos** (Puerto 8085)
Gestión de pedidos de equipos.

**Funcionalidades:**
- Creación de pedidos
- Asociación con usuarios y equipos
- Tipos de pedidos (Compra, Renta, etc.)
- Seguimiento del estado del pedido
- Integración con inventario

**Endpoints:**
- `GET /api/v1/pedidos` - Obtener todos
- `GET /api/v1/pedidos/{id}` - Obtener por ID
- `POST /api/v1/pedidos/crear` - Crear
- `PUT /api/v1/pedidos/modificar/{id}` - Actualizar
- `DELETE /api/v1/pedidos/eliminar/{id}` - Eliminar

---

### 7. **PagoFactura** (Puerto 8087)
Gestión de pagos e invoices.

**Funcionalidades:**
- Creación de facturas
- Registros de pagos
- Cálculo de montos
- Seguimiento de transacciones
- Integración con pedidos

**Endpoints:**
- `GET /api/v1/facturas` - Obtener facturas
- `POST /api/v1/facturas` - Crear factura
- `PUT /api/v1/facturas/{id}` - Actualizar factura
- `DELETE /api/v1/facturas/{id}` - Eliminar factura
- `GET /api/v1/pagos` - Obtener pagos
- `POST /api/v1/pagos` - Crear pago
- `PUT /api/v1/pagos/{id}` - Actualizar pago
- `DELETE /api/v1/pagos/{id}` - Eliminar pago

---

### 8. **GestorEntregas** (Puerto 8088)
Gestión de entregas y logística.

**Funcionalidades:**
- Creación de entregas
- Seguimiento de estado (Pendiente, En Tránsito, Entregado)
- Integración con pedidos
- Asignación de fechas de entrega

**Endpoints:**
- `GET /api/v1/entregas` - Obtener todas
- `GET /api/v1/entregas/{id}` - Obtener por ID
- `POST /api/v1/entregas/crear` - Crear
- `PUT /api/v1/entregas/actualizar/{id}` - Actualizar
- `DELETE /api/v1/entregas/eliminar/{id}` - Eliminar

---

### 9. **SoporteTecnico** (Puerto 8084)
Sistema de soporte técnico.

**Funcionalidades:**
- Creación de tickets de soporte
- Gestión de tipos de soporte
- Asignación de tickets
- Seguimiento de estado
- Historial de soportes

**Endpoints:**
- `GET /api/v1/tickets` - Obtener tickets
- `GET /api/v1/tickets/{id}` - Obtener por ID
- `POST /api/v1/tickets` - Crear ticket
- `PUT /api/v1/tickets/{id}` - Actualizar ticket
- `DELETE /api/v1/tickets/{id}` - Eliminar ticket

---

##  Tecnologías Utilizadas

### Backend
- **Spring Boot 3.5.3** - Framework principal
- **Spring Data JPA** - ORM para persistencia
- **Hibernate** - Motor de ORM
- **Spring WebFlux** - WebClient para comunicación inter-servicios
- **Spring Security** - Autenticación y autorización
- **JWT (JJWT 0.11.5)** - Tokens JWT

### Persistencia
- **MySQL 8** - Base de datos relacional
- **MySQL Connector/J** - Driver JDBC

### Documentación & UI
- **SpringDoc OpenAPI 3** - Generación automática de documentación
- **Swagger UI** - Interfaz visual para APIs

### Utilidades
- **Lombok** - Reduce boilerplate (getters, setters, constructores)
- **Maven** - Gestor de dependencias

### Testing
- **JUnit 5** - Framework de testing
- **Spring Test** - Utilidades de test para Spring
- **MockMvc** - Testing de controladores

---

##  Base de Datos

### Configuración
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agrotech_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Tablas Principales
- `usuarios` - Gestión de usuarios
- `roles` - Roles del sistema
- `permisos` - Permisos disponibles
- `role_permiso` - Asociación ManyToMany
- `equipos` - Inventario de equipos
- `estados` - Estados de equipos
- `marcas` - Marcas de equipos
- `modelos` - Modelos de equipos
- `pedidos` - Órdenes de compra
- `tipos` - Tipos de pedidos
- `facturas` - Facturas de pago
- `pagos` - Registros de pago
- `entregas` - Registro de entregas
- `tickets` - Tickets de soporte
- `usuario_conectado` - Sesiones activas

---

##  Pasos para Ejecutar

### Prerequisitos
- **Java 21+** instalado
- **MySQL 8+** instalado y ejecutándose
- **Git** instalado
- **Maven 3.8+** instalado
- **Postman** (opcional, para pruebas de API)

### 1. Clonar o descargar el proyecto
```bash
git clone [https://github.com/BrunoMirandaR/full_Stack_1.git](https://github.com/BrunoMirandaR/full_Stack_1.git)
```

### 2. Crear la base de datos
```Pasos rápidos en HeidiSQL/Laragon para ejecutarlo:
Conéctate a tu sesión local de Laragon en HeidiSQL.

Haz clic derecho sobre tu conexión (en la raíz del árbol de la izquierda) y selecciona Nueva pestaña de consulta (o presiona Ctrl + T).

Pega el código de arriba.

Presiona la tecla F9 o haz clic en el botón azul de Play en la barra de herramientas para ejecutarlo.

Haz clic derecho en el árbol de la izquierda y selecciona Refrescar (F5) para ver tu ecosistema de microservicios creado y listo para recibir las tablas desde Spring Boot.

CREATE DATABASE IF NOT EXISTS agrotech_db_autenticacion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_gestion_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_gestor_entregas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_gestor_inventario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_gestor_marca_modelo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_gestor_pedidos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_pago_factura CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_roles_permisos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS agrotech_db_soporte_tecnico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

*Nota: Las tablas se crearán automáticamente con `spring.jpa.hibernate.ddl-auto=update`*

### 3. Iniciar los servicios en orden de dependencias

#### **Abre la carpeta del microservicio específico en tu VisualStudioCode.

Navega en el árbol de archivos hasta la ruta del código fuente principal. Tomando como ejemplo tu servicio de Marcas y Modelos, la ruta exacta es:
src/main/java/com/example/GestorMarcaYModelo/GestorMarcaYModeloApplication.java

Abre ese archivo .java.

Busca el método public static void main(String[] args).

Verás un botón flotante de texto arriba del método que dice "Run" o "Debug" (o puedes hacer clic derecho en cualquier parte del archivo y seleccionar Run Java).

Al ejecutarlo, Spring Boot detectará la propiedad ddl-auto=update, creará automáticamente las tablas en la base de datos correspondiente de HeidiSQL y finalmente el componente LoadDatabase.java poblará los datos iniciales.

### 4. Verificar que todos los servicios estén corriendo

Cada servicio debería mostrar en la terminal:
```
Started [ServiceName]Application in X.XXX seconds
```

### 5. Acceder a la documentación Swagger

- AutenticacionNuevo: http://localhost:8082/swagger-ui.html
- GestionDeUsuarioNuevo: http://localhost:8081/swagger-ui.html
- RolesyPermisos: http://localhost:8089/swagger-ui.html
- GestorMarcaYModelo: http://localhost:8086/swagger-ui.html
- GestorInventario: http://localhost:8083/swagger-ui.html
- GestorPedidos: http://localhost:8085/swagger-ui.html
- PagoFactura: http://localhost:8087/swagger-ui.html
- GestorEntregas: http://localhost:8088/swagger-ui.html
- SoporteTecnico: http://localhost:8084/swagger-ui.html

### 6. Realizar pruebas con Postman

**Paso 1: Login**
```
POST http://localhost:8082/auth/login
```

**Paso 2: Obtener usuarios conectados**
```
GET http://localhost:8082/auth/conectados
```

*Ver archivo `pruebas_para_postman.txt` para ejemplos completos de cada endpoint*

---

##  Flujo de Negocio

### Scenario 1: Crear un Pedido Completo

1. **Login** en AutenticacionNuevo
2. **Obtener ID** de usuario conectado
3. **Crear Marca** en GestorMarcaYModelo
4. **Crear Modelo** en GestorMarcaYModelo
5. **Crear Equipo** en GestorInventario
6. **Crear Pedido** en GestorPedidos
7. **Crear Factura** en PagoFactura
8. **Crear Entrega** en GestorEntregas

### Scenario 2: Asignar Permisos a Rol

1. Obtener roles disponibles en RolesyPermisos
2. Obtener permisos disponibles en RolesyPermisos
3. Crear nuevo rol o actualizar existente
4. Asignar permisos al rol
5. Asignar rol a usuario en GestionDeUsuarioNuevo

---

##  Seguridad

### Autenticación
- **JWT Tokens** - Tokens seguros generados en login
- **X-User-Id Header** - ID de usuario propagado entre servicios
- **Spring Security** - Configuración de seguridad

### Autorización
- **Roles y Permisos** - Control granular de acceso
- **Validación en cada endpoint** - Verificación de usuario conectado
- **Roles Predefinidos** - 6 roles con permisos específicos

### Headers Requeridos
```
X-User-Id: {idUserConectado}
Content-Type: application/json (para POST/PUT)
```

---

##  Troubleshooting

### Error: Connection refused on port 8082
**Causa:** AutenticacionNuevo no está corriendo
**Solución:** Inicia AutenticacionNuevo primero

### Error: 404 usuario-conectado-service
**Causa:** El header X-User-Id no está incluido o es inválido
**Solución:** Asegúrate de obtener el ID del usuario conectado antes

### Error: Fallo al obtener usuario conectado
**Causa:** Un servicio dependiente no está disponible
**Solución:** Verifica que todos los servicios necesarios estén corriendo

### Error: Port already in use
**Causa:** El puerto ya está ocupado
**Solución:** Mata el proceso anterior o usa un puerto diferente

---

##  Estadísticas del Proyecto

- **Total de Microservicios:** 9
- **Endpoints totales:** 60+
- **Tablas de BD:** 15+
- **Roles predefinidos:** 6
- **Permisos:** 25+
- **DTOs:** 10+
- **Tests:** 45+ test classes



##  Licencia

Este proyecto es de uso educativo para el curso Full Stack 1 de DUOC UC.

---

##  Soporte

Para preguntas o problemas, contacta a los desarrolladores del equipo o revisa la documentación de Swagger en cada servicio.

---

##  Checklist de Verificación

- [ ] Java 21+ instalado
- [ ] MySQL 8+ corriendo
- [ ] Bases de datos `agrotech_db` creadas
- [ ] Maven compiló todos los servicios
- [ ] AutenticacionNuevo iniciado (8082)
- [ ] RolesyPermisos iniciado (8089)
- [ ] GestionDeUsuarioNuevo iniciado (8081)
- [ ] GestorMarcaYModelo iniciado (8086)
- [ ] GestorInventario iniciado (8083)
- [ ] GestorPedidos iniciado (8085)
- [ ] PagoFactura iniciado (8087)
- [ ] GestorEntregas iniciado (8088)
- [ ] SoporteTecnico iniciado (8084)
- [ ] Login exitoso en http://localhost:8082/auth/login
- [ ] Swagger accesible en los puertos correspondientes

---

**Última actualización:** Junio 2026
**Versión:** 1.0.0
