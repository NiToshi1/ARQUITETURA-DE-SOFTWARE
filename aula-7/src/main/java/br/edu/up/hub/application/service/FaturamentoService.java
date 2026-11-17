package br.edu.up.hub.application.service;

import br.edu.up.hub.domain.model.EmissorIndisponivelException;
import br.edu.up.hub.domain.model.Fatura;
import br.edu.up.hub.domain.model.PedidoFaturamento;
import br.edu.up.hub.domain.ports.in.FaturarPedidoUseCase;
import br.edu.up.hub.domain.ports.out.*;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Camada de aplicação: implementa a porta de entrada e usa as portas de saída.
 * Nunca fala com a tecnologia diretamente, e decide o que fazer quando uma
 * porta não responde.
 */
@Service
public class FaturamentoService implements FaturarPedidoUseCase {

    private final FaturaRepositoryPort faturaRepository;
    private final BoletoQueuePort boletoQueuePort;
    private final NotaFiscalSoapPort notaFiscalSoapPort;
    private final NotificacaoSmtpPort notificacaoSmtpPort;

    public FaturamentoService(FaturaRepositoryPort faturaRepository,
                              BoletoQueuePort boletoQueuePort,
                              NotaFiscalSoapPort notaFiscalSoapPort,
                              NotificacaoSmtpPort notificacaoSmtpPort) {
        this.faturaRepository = faturaRepository;
        this.boletoQueuePort = boletoQueuePort;
        this.notaFiscalSoapPort = notaFiscalSoapPort;
        this.notificacaoSmtpPort = notificacaoSmtpPort;
    }

    @Override
    public String faturar(PedidoFaturamento pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido de faturamento não informado.");
        }

        // RF01: cria a fatura (a entidade valida as regras) e guarda no banco
        Fatura fatura = new Fatura(pedido.pedidoId(), pedido.clienteEmail(), pedido.valorTotal());
        chamar("banco de dados", () -> faturaRepository.salvar(fatura));

        // RF02: boleto pela fila de pedidos
        String codigoBarras = chamar("emissor de boleto (fila)", () -> boletoQueuePort.emitirBoletoPorFila(fatura));
        fatura.registrarBoleto(codigoBarras);
        chamar("banco de dados", () -> faturaRepository.salvar(fatura));

        // RF03: nota fiscal por SOAP (chave de 44 dígitos)
        String chaveAcesso = chamar("emissor de nota fiscal (SOAP)", () -> notaFiscalSoapPort.emitirNotaFiscalSoap(fatura));
        fatura.registrarNotaFiscal(chaveAcesso);
        chamar("banco de dados", () -> faturaRepository.salvar(fatura));

        // RF04: e-mail ao cliente por SMTP (guarda o hash do conteúdo)
        String hashEmail = chamar("serviço de e-mail (SMTP)", () -> notificacaoSmtpPort.enviarEmailNotificacao(fatura));
        fatura.registrarNotificacao(hashEmail);
        chamar("banco de dados", () -> faturaRepository.salvar(fatura));

        return fatura.getId();
    }

    /** Regra da aplicação: se uma porta de saída falha, traduz para uma falha do negócio. */
    private <T> T chamar(String emissor, Supplier<T> chamada) {
        try {
            return chamada.get();
        } catch (RuntimeException e) {
            throw new EmissorIndisponivelException(emissor, e);
        }
    }
}
