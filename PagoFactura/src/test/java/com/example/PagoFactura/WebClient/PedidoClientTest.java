package com.example.PagoFactura.WebClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

class PedidoClientTest {

    private static HttpServer httpServer;
    private static String baseUrl;

    @BeforeAll
    static void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(0), 0);
        httpServer.createContext("/api/v1/pedidos", new PedidoHandler());
        httpServer.start();
        baseUrl = "http://localhost:" + httpServer.getAddress().getPort() + "/api/v1/pedidos";
    }

    @AfterAll
    static void stopServer() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }

    @Test
    void obtenerPedidoPorId_shouldReturnPedido() {
        PedidoClient pedidoClient = new PedidoClient(baseUrl);

        Optional<Map<String, Object>> pedido = pedidoClient.obtenerPedidoPorId(1);

        assertThat(pedido).isPresent();
        assertThat(pedido.orElseThrow().get("total")).isEqualTo(1500.0);
    }

    @Test
    void obtenerPedidoPorId_shouldReturnEmptyOnError() {
        PedidoClient pedidoClient = new PedidoClient(baseUrl);

        Optional<Map<String, Object>> pedido = pedidoClient.obtenerPedidoPorId(99);

        assertThat(pedido).isEmpty();
    }

    @Test
    void obtenerPedidoPorId_shouldHandleInvalidJsonGracefully() {
        PedidoClient pedidoClient = new PedidoClient(baseUrl);

        Optional<Map<String, Object>> pedido = pedidoClient.obtenerPedidoPorId(2);

        assertThat(pedido).isEmpty();
    }

    private static class PedidoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String response;
            int status;

            if ("/api/v1/pedidos/1".equals(path)) {
                status = 200;
                response = "{\"idPedido\":1,\"total\":1500.0}";
            } else if ("/api/v1/pedidos/2".equals(path)) {
                status = 200;
                response = "not-json";
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