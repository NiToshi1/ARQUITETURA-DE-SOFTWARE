package br.edu.up.mvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteNome;
    private String clienteEmail;
    private BigDecimal valorTotal;
    private String status;
    private String faturaId;

    public Pedido() {
        this.status = "ABERTO";
    }

    public void fechar(String faturaId) {
        this.faturaId = faturaId;
        this.status = "FECHADO";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFaturaId() { return faturaId; }
    public void setFaturaId(String faturaId) { this.faturaId = faturaId; }
}
