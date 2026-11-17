package br.edu.up.hub.domain.ports.out;

import br.edu.up.hub.domain.model.Fatura;
import java.util.Optional;

public interface FaturaRepositoryPort {
    Fatura salvar(Fatura fatura);
    Optional<Fatura> buscarPorId(String id);
}
