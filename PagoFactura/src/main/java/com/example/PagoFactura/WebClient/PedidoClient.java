package com.example.PagoFactura.WebClient;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class PedidoClient {
    private final WebClient webClient;
        private final String pedidoServiceBaseUrl;

    public PedidoClient(@Value("${pedido-service.base-url}") String pedidoServiceBaseUrl) {
        this.pedidoServiceBaseUrl = pedidoServiceBaseUrl; // <--- Guardas el valor inyectado
        this.webClient = WebClient.builder()
                .baseUrl(pedidoServiceBaseUrl)
                .build();
    }

    public Optional<Map<String, Object>> obtenerPedidoPorId(Integer idPedido) {
        try {
            Map<String, Object> pedido = webClient.get()
                    .uri("/{idPedido}", idPedido)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        System.err.println("Error HTTP al obtener pedido: "
                                                + clientResponse.statusCode() + ", body: " + body);
                                        return Mono.error(new RuntimeException("Error al obtener pedido: " + body));
                                    }))
                    .bodyToMono(Map.class)
                    .doOnSubscribe(s -> System.out.println("Consultando pedido id " + idPedido))
                    .doOnNext(body -> System.out.println("Pedido obtenido: " + body))
                    .doOnNext(body -> System.out.println("Pedido obtenido en cliente: " + body))
                    .block();

            return Optional.ofNullable(pedido);

        } catch (Exception e) {
            System.err.println("Error al buscar pedido por ID " + idPedido + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
