package br.edu.up.hub.adapters.in.web;

import br.edu.up.hub.domain.model.PedidoFaturamento;
import br.edu.up.hub.domain.ports.in.FaturarPedidoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/faturas")
public class FaturamentoController {

    private final FaturarPedidoUseCase faturarPedidoUseCase;

    public FaturamentoController(FaturarPedidoUseCase faturarPedidoUseCase) {
        this.faturarPedidoUseCase = faturarPedidoUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> faturarPedido(@RequestBody PedidoFaturamento pedido) {
        String faturaId = faturarPedidoUseCase.faturar(pedido);
        return ResponseEntity.ok(Map.of("faturaId", faturaId));
    }
}
