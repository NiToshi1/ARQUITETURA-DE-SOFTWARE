package br.edu.up.hub.domain.ports.out;

import br.edu.up.hub.domain.model.Fatura;

public interface NotificacaoSmtpPort {
    String enviarEmailNotificacao(Fatura fatura);
}
