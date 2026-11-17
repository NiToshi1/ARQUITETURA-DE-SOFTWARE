package br.edu.up.mvc.client;

import br.edu.up.mvc.model.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class FaturamentoHubClient {

    private final RestTemplate restTemplate;
    private final String hubUrl;

    public FaturamentoHubClient(@Value("${hub.faturamento.url:http://localhost:8081/api/faturas}") String hubUrl) {
        this.restTemplate = new RestTemplate();
        this.hubUrl = hubUrl;
    }

    public String solicitarFaturamento(Pedido pedido) {
        Map<String, Object> payload = Map.of(
                "pedidoId", pedido.getId(),
                "clienteNome", pedido.getClienteNome(),
                "clienteEmail", pedido.getClienteEmail(),
                "valorTotal", pedido.getValorTotal()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                hubUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, String> body = response.getBody();
        if (body == null || !body.containsKey("faturaId")) {
            throw new IllegalStateException("Hub de faturamento não devolveu o ID da fatura.");
        }
        return body.get("faturaId");
    }
}
