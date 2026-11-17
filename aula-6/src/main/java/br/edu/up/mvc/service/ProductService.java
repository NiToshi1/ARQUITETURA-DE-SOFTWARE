package br.edu.up.mvc.service;

import br.edu.up.mvc.model.Product;
import br.edu.up.mvc.model.ProductStatus;
import br.edu.up.mvc.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listarTodos() {
        return productRepository.findAll();
    }

    public Product cadastrar(Product product) {
        product.setQuantidadeEstoque(0);
        product.setStatus(ProductStatus.INDISPONIVEL);
        return productRepository.save(product);
    }
}
