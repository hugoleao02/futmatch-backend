package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.ListarPartidasUseCase;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarPartidasUseCaseImpl implements ListarPartidasUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final PartidaMapper partidaMapper;

    public ListarPartidasUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                     PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public List<PartidaResponse> listarPartidas() {
        return partidaRepositoryPort.findAll().stream()
                .map(partidaMapper::toResponseFull)
                .collect(Collectors.toList());
    }
}
