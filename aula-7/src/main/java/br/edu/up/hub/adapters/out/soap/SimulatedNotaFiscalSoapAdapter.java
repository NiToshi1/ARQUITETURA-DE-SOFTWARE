package br.edu.up.hub.adapters.out.soap;

import br.edu.up.hub.domain.model.Fatura;
import br.edu.up.hub.domain.ports.out.NotaFiscalSoapPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimulatedNotaFiscalSoapAdapter implements NotaFiscalSoapPort {

    @Override
    public String emitirNotaFiscalSoap(Fatura fatura) {
        StringBuilder chave44 = new StringBuilder("412611");
        while (chave44.length() < 44) {
            chave44.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        String chave = chave44.toString();
        System.out.println("[SOAP-NFE] NF-e emitida para fatura " + fatura.getId() + " | Chave (44 dígitos): " + chave);
        return chave;
    }
}
