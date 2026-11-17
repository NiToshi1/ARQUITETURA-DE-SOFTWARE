package br.edu.up.hub.adapters.out.persistence;

import br.edu.up.hub.domain.model.Fatura;
import br.edu.up.hub.domain.ports.out.FaturaRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimulatedFaturaRepositoryAdapter implements FaturaRepositoryPort {

    private final Map<String, Fatura> bancoEmMemoria = new ConcurrentHashMap<>();

    @Override
    public Fatura salvar(Fatura fatura) {
        bancoEmMemoria.put(fatura.getId(), fatura);
        return fatura;
    }

    @Override
    public Optional<Fatura> buscarPorId(String id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }
}
