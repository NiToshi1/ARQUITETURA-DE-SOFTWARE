package br.edu.up.mvc.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double preco;
    private Integer quantidadeEstoque;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product() {
        this.quantidadeEstoque = 0;
        this.status = ProductStatus.INDISPONIVEL;
    }

    public Product(String nome, Double preco) {
        this();
        this.nome = nome;
        this.preco = preco;
    }

    public void darEntrada(int quantidade) {
        if (this.status == ProductStatus.DESCONTINUADO) {
            throw new IllegalStateException("Produto descontinuado não pode ser manipulado no estoque.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser maior que zero.");
        }
        boolean estavaZerado = (this.quantidadeEstoque == 0);
        this.quantidadeEstoque += quantidade;

        if (estavaZerado && this.quantidadeEstoque > 0) {
            this.status = ProductStatus.DISPONIVEL;
        }
    }

    public void darSaida(int quantidade) {
        if (this.status == ProductStatus.DESCONTINUADO) {
            throw new IllegalStateException("Produto descontinuado não pode ser manipulado no estoque.");
        }
        if (this.status != ProductStatus.DISPONIVEL || this.quantidadeEstoque < quantidade) {
            throw new IllegalStateException("Estoque insuficiente ou produto indisponível para saída.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de saída deve ser maior que zero.");
        }

        this.quantidadeEstoque -= quantidade;

        if (this.quantidadeEstoque == 0) {
            this.status = ProductStatus.INDISPONIVEL;
        }
    }

    public void descontinuar(boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("Apenas o administrador pode descontinuar um produto.");
        }
        this.status = ProductStatus.DESCONTINUADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
}
