package br.edu.up.mvc.repository;

import br.edu.up.mvc.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
