package br.edu.up.hub.adapters.out.queue;

import br.edu.up.hub.domain.model.Fatura;
import br.edu.up.hub.domain.ports.out.BoletoQueuePort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimulatedBoletoQueueAdapter implements BoletoQueuePort {

    @Override
    public String emitirBoletoPorFila(Fatura fatura) {
        long seq = ThreadLocalRandom.current().nextLong(100000000000L, 999999999999L);
        String codigoBarras = "34191.79001 01043.510047 91020.150008 1 " + seq;
        System.out.println("[FILA-BOLETO] Boleto emitido para fatura " + fatura.getId() + ": " + codigoBarras);
        return codigoBarras;
    }
}
