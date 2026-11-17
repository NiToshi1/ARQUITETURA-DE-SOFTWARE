package br.edu.up.mvc.controller;

import br.edu.up.mvc.model.Pedido;
import br.edu.up.mvc.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        model.addAttribute("novoPedido", new Pedido());
        return "pedidos";
    }

    @PostMapping
    public String criarPedido(@ModelAttribute Pedido novoPedido) {
        pedidoService.criarPedido(novoPedido);
        return "redirect:/pedidos";
    }

    @PostMapping("/{id}/fechar")
    public String fecharPedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.fecharPedido(id);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Pedido fechado com sucesso! Fatura gerada: " + pedido.getFaturaId());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Falha ao faturar pedido no Hub: " + e.getMessage());
        }
        return "redirect:/pedidos";
    }
}
