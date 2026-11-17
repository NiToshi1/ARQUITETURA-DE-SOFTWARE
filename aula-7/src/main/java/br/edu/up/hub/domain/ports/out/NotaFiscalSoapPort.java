package br.edu.up.hub.domain.ports.out;

import br.edu.up.hub.domain.model.Fatura;

public interface NotaFiscalSoapPort {
    String emitirNotaFiscalSoap(Fatura fatura);
}
