package br.edu.up.mvc.controller;

import br.edu.up.mvc.model.Product;
import br.edu.up.mvc.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", productService.listarTodos());
        model.addAttribute("novoProduto", new Product());
        return "products";
    }

    @PostMapping
    public String cadastrar(@ModelAttribute Product novoProduto) {
        productService.cadastrar(novoProduto);
        return "redirect:/products";
    }
}
