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

class MarcaClientTest {

    private static HttpServer httpServer;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext("/api/v1/marcas", new MarcaHandler());
        httpServer.start();
        baseUrl = "http://localhost:" + httpServer.getAddress().getPort() + "/api/v1/marcas";
    }

    @AfterAll
    static void stopServer() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @Test
    void obtenerMarcaPorId_shouldReturnMarca() {
        MarcaClient marcaClient = new MarcaClient(baseUrl);

        Map<String, Object> marca = marcaClient.obtenerMarcaPorId(1);

        assertThat(marca.get("idMarca")).isEqualTo(1);
        assertThat(marca.get("nombre")).isEqualTo("John Deere");
    }

    @Test
    void obtenerTodasLasMarcas_shouldReturnList() {
        MarcaClient marcaClient = new MarcaClient(baseUrl);

        List<Map<String, Object>> marcas = marcaClient.obtenerTodasLasMarcas();

        assertThat(marcas).hasSize(2);
        assertThat(marcas.get(0).get("nombre")).isEqualTo("John Deere");
    }

    @Test
    void obtenerMarcaPorId_shouldThrowOnClientError() {
        MarcaClient marcaClient = new MarcaClient(baseUrl);

        assertThatThrownBy(() -> marcaClient.obtenerMarcaPorId(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Marca no encontrada");
    }

    private static class MarcaHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response;
            int status;

            if ("/api/v1/marcas/1".equals(path)) {
                status = 200;
                response = "{\"idMarca\":1,\"nombre\":\"John Deere\"}";
            } else if ("/api/v1/marcas".equals(path)) {
                status = 200;
                response = "["
                        + "{\"idMarca\":1,\"nombre\":\"John Deere\"},"
                        + "{\"idMarca\":2,\"nombre\":\"Case IH\"}"
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