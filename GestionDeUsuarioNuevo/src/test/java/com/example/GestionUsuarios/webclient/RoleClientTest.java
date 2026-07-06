package com.example.GestionUsuarios.webclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class RoleClientTest {

    private static HttpServer httpServer;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext("/api/v1/roles", new RoleHandler());
        httpServer.start();
        baseUrl = "http://localhost:" + httpServer.getAddress().getPort() + "/api/v1/roles";
    }

    @AfterAll
    static void stopServer() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @Test
    void obtenerRolPorNombre_shouldReturnRoleAndSendHeader() {
        RoleClient roleClient = new RoleClient(baseUrl);

        Map<String, Object> role = roleClient.obtenerRolPorNombre("Administrador", 15);

        assertThat(role.get("idRole")).isEqualTo(1);
        assertThat(role.get("nombre")).isEqualTo("Administrador");
    }

    @Test
    void obtenerRolPorId_shouldReturnRole() {
        RoleClient roleClient = new RoleClient(baseUrl);

        Map<String, Object> role = roleClient.obtenerRolPorId(2, 99);

        assertThat(role.get("idRole")).isEqualTo(2);
        assertThat(role.get("nombre")).isEqualTo("Gestor de Inventario");
    }

    @Test
    void obtenerRolPorNombreSinValidacion_shouldReturnRole() {
        RoleClient roleClient = new RoleClient(baseUrl);

        Map<String, Object> role = roleClient.obtenerRolPorNombreSinValidacion("Soporte Técnico");

        assertThat(role.get("idRole")).isEqualTo(4);
        assertThat(role.get("nombre")).isEqualTo("Soporte Técnico");
    }

    @Test
    void obtenerRolPorNombre_shouldThrowOnClientError() {
        RoleClient roleClient = new RoleClient(baseUrl);

        assertThatThrownBy(() -> roleClient.obtenerRolPorNombre("NoExiste", 15))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error al obtener rol");
    }

    private static class RoleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response;
            int status;

            if ("/api/v1/roles/nombre/Administrador".equals(path)) {
                status = 200;
                response = "{\"idRole\":1,\"nombre\":\"Administrador\"}";
            } else if ("/api/v1/roles/2".equals(path)) {
                status = 200;
                response = "{\"idRole\":2,\"nombre\":\"Gestor de Inventario\"}";
            } else if ("/api/v1/roles/nombre/Soporte Técnico".equals(path)) {
                status = 200;
                response = "{\"idRole\":4,\"nombre\":\"Soporte Técnico\"}";
            } else {
                status = 404;
                response = "{\"error\":\"not found\"}";
            }

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}