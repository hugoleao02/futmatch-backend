package br.com.futmatch.domain.ports;

import br.com.futmatch.domain.models.Partida;

public interface PartidaRepositoryPort {
    Partida save(Partida partida);
} 