package br.edu.up.hub.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade do domínio. Guarda as validações de negócio:
 * uma Fatura com valor zero (ou sem pedido/e-mail) não consegue existir.
 * Não conhece nenhuma tecnologia.
 */
public class Fatura {

    private final String id;
    private final Long pedidoId;
    private final String clienteEmail;
    private final BigDecimal valor;
    private String codigoBarrasBoleto;
    private String chaveAcessoNfe;
    private String hashEmailEnviado;
    private String status;

    public Fatura(Long pedidoId, String clienteEmail, BigDecimal valor) {
        if (pedidoId == null) {
            throw new IllegalArgumentException("A fatura precisa estar ligada a um pedido.");
        }
        if (clienteEmail == null || clienteEmail.isBlank()) {
            throw new IllegalArgumentException("A fatura precisa do e-mail do cliente.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da fatura deve ser maior que zero.");
        }
        this.id = UUID.randomUUID().toString();
        this.pedidoId = pedidoId;
        this.clienteEmail = clienteEmail;
        this.valor = valor;
        this.status = "CRIADA";
    }

    public void registrarBoleto(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            throw new IllegalArgumentException("Código de barras do boleto inválido.");
        }
        this.codigoBarrasBoleto = codigoBarras;
        this.status = "COM_BOLETO";
    }

    public void registrarNotaFiscal(String chaveAcesso44Digitos) {
        if (chaveAcesso44Digitos == null || !chaveAcesso44Digitos.matches("\\d{44}")) {
            throw new IllegalArgumentException("Chave de acesso da NF-e deve conter exatamente 44 dígitos.");
        }
        this.chaveAcessoNfe = chaveAcesso44Digitos;
        this.status = "COM_NOTA";
    }

    public void registrarNotificacao(String hashConteudo) {
        if (codigoBarrasBoleto == null || chaveAcessoNfe == null) {
            throw new IllegalStateException("Só é possível notificar uma fatura com boleto e nota fiscal.");
        }
        this.hashEmailEnviado = hashConteudo;
        this.status = "FINALIZADA";
    }

    public String getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public String getClienteEmail() { return clienteEmail; }
    public BigDecimal getValor() { return valor; }
    public String getCodigoBarrasBoleto() { return codigoBarrasBoleto; }
    public String getChaveAcessoNfe() { return chaveAcessoNfe; }
    public String getHashEmailEnviado() { return hashEmailEnviado; }
    public String getStatus() { return status; }
}
