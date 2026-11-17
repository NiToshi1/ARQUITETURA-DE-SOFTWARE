package br.edu.up.hub.adapters.out.smtp;

import br.edu.up.hub.domain.model.Fatura;
import br.edu.up.hub.domain.ports.out.NotificacaoSmtpPort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class SimulatedNotificacaoSmtpAdapter implements NotificacaoSmtpPort {

    @Override
    public String enviarEmailNotificacao(Fatura fatura) {
        String conteudoEmail = String.format(
                "Para: %s | Fatura: %s | Boleto: %s | NF-e: %s",
                fatura.getClienteEmail(),
                fatura.getId(),
                fatura.getCodigoBarrasBoleto(),
                fatura.getChaveAcessoNfe()
        );
        String hash = gerarSha256(conteudoEmail);
        System.out.println("[SMTP-EMAIL] E-mail enviado: " + conteudoEmail + " | Hash: " + hash);
        return hash;
    }

    private String gerarSha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encoded);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash SHA-256 do e-mail", e);
        }
    }
}
