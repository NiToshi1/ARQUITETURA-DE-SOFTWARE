package br.edu.up.mvc.service;

import br.edu.up.mvc.model.Product;
import br.edu.up.mvc.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueService {

    private final ProductRepository productRepository;

    public EstoqueService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listarProdutos() {
        return productRepository.findAll();
    }

    @Transactional
    public Product registrarEntrada(Long productId, int quantidade) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        product.darEntrada(quantidade);
        return productRepository.save(product);
    }

    @Transactional
    public Product registrarSaida(Long productId, int quantidade) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        product.darSaida(quantidade);
        return productRepository.save(product);
    }

    @Transactional
    public Product descontinuarProduto(Long productId, boolean isAdmin) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        product.descontinuar(isAdmin);
        return productRepository.save(product);
    }
}
