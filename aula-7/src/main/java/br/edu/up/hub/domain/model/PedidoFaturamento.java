package br.edu.up.hub.domain.model;

import java.math.BigDecimal;

public record PedidoFaturamento(
        Long pedidoId,
        String clienteNome,
        String clienteEmail,
        BigDecimal valorTotal
) {}
