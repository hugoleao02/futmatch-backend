package br.com.futmatch.domain.ports;

import br.com.futmatch.domain.models.Partida;
import java.util.List;
import java.util.Optional;

public interface PartidaRepositoryPort {
    Partida save(Partida partida);
    Optional<Partida> findById(Long id);
    Partida update(Partida partida);
    List<Partida> findAll();
} 