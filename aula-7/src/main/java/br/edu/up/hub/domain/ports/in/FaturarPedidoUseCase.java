package br.edu.up.hub.domain.ports.in;

import br.edu.up.hub.domain.model.PedidoFaturamento;

public interface FaturarPedidoUseCase {
    String faturar(PedidoFaturamento pedido);
}
