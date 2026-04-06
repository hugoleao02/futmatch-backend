package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.BuscarPartidaPorIdUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.stereotype.Service;

@Service
public class BuscarPartidaPorIdUseCaseImpl implements BuscarPartidaPorIdUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final PartidaMapper partidaMapper;

    public BuscarPartidaPorIdUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                         PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public PartidaResponse buscarPartidaPorId(Long id) {
        Partida partida = partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + id));
        return partidaMapper.toResponseFull(partida);
    }
}
