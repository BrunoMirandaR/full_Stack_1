package com.example.GestorInventario.webclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class ModeloClientTest {

    private static HttpServer httpServer;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext("/api/v1/modelos", new ModeloHandler());
        httpServer.start();
        baseUrl = "http://localhost:" + httpServer.getAddress().getPort() + "/api/v1/modelos";
    }

    @AfterAll
    static void stopServer() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @Test
    void obtenerModeloPorId_shouldReturnModelo() {
        ModeloClient modeloClient = new ModeloClient(baseUrl);

        Map<String, Object> modelo = modeloClient.obtenerModeloPorId(1);

        assertThat(modelo.get("idModelo")).isEqualTo(1);
        assertThat(modelo.get("nombre")).isEqualTo("5055E");
    }

    @Test
    void obtenerTodosLosModelos_shouldReturnList() {
        ModeloClient modeloClient = new ModeloClient(baseUrl);

        List<Map<String, Object>> modelos = modeloClient.obtenerTodosLosModelos();

        assertThat(modelos).hasSize(2);
        assertThat(modelos.get(1).get("nombre")).isEqualTo("Puma 185");
    }

    @Test
    void obtenerModeloPorId_shouldThrowOnClientError() {
        ModeloClient modeloClient = new ModeloClient(baseUrl);

        assertThatThrownBy(() -> modeloClient.obtenerModeloPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Modelo no encontrado");
    }

    private static class ModeloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response;
            int status;

            if ("/api/v1/modelos/1".equals(path)) {
                status = 200;
                response = "{\"idModelo\":1,\"nombre\":\"5055E\"}";
            } else if ("/api/v1/modelos".equals(path)) {
                status = 200;
                response = "["
                        + "{\"idModelo\":1,\"nombre\":\"5055E\"},"
                        + "{\"idModelo\":5,\"nombre\":\"Puma 185\"}"
                        + "]";
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