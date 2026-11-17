package br.edu.up.mvc.controller;

import br.edu.up.mvc.service.EstoqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public String telaEstoque(Model model) {
        model.addAttribute("produtos", estoqueService.listarProdutos());
        return "estoque";
    }

    @PostMapping("/entrada")
    public String entradaEstoque(@RequestParam Long productId,
                                 @RequestParam int quantidade,
                                 RedirectAttributes redirectAttributes) {
        try {
            estoqueService.registrarEntrada(productId, quantidade);
            redirectAttributes.addFlashAttribute("sucesso", "Entrada registrada com sucesso.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/estoque";
    }

    @PostMapping("/saida")
    public String saidaEstoque(@RequestParam Long productId,
                               @RequestParam int quantidade,
                               RedirectAttributes redirectAttributes) {
        try {
            estoqueService.registrarSaida(productId, quantidade);
            redirectAttributes.addFlashAttribute("sucesso", "Saída registrada com sucesso.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/estoque";
    }

    @PostMapping("/descontinuar")
    public String descontinuar(@RequestParam Long productId,
                               @RequestParam(defaultValue = "false") boolean isAdmin,
                               RedirectAttributes redirectAttributes) {
        try {
            estoqueService.descontinuarProduto(productId, isAdmin);
            redirectAttributes.addFlashAttribute("sucesso", "Produto descontinuado.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/estoque";
    }
}
