package br.edu.up.mvc.service;

import br.edu.up.mvc.client.FaturamentoHubClient;
import br.edu.up.mvc.model.Pedido;
import br.edu.up.mvc.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FaturamentoHubClient faturamentoHubClient;

    public PedidoService(PedidoRepository pedidoRepository,
                         FaturamentoHubClient faturamentoHubClient) {
        this.pedidoRepository = pedidoRepository;
        this.faturamentoHubClient = faturamentoHubClient;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido criarPedido(Pedido pedido) {
        pedido.setStatus("ABERTO");
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido fecharPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));

        if ("FECHADO".equals(pedido.getStatus())) {
            throw new IllegalStateException("Pedido já está fechado.");
        }

        String faturaId = faturamentoHubClient.solicitarFaturamento(pedido);
        pedido.fechar(faturaId);

        return pedidoRepository.save(pedido);
    }
}
