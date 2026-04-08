package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.ListarPartidasFuturasUseCase;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListarPartidasFuturasUseCaseImpl implements ListarPartidasFuturasUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final PartidaMapper partidaMapper;

    public ListarPartidasFuturasUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                            PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public Page<PartidaResponse> listarPartidasFuturas(Pageable pageable) {
        return partidaRepositoryPort.findAllFuturas(pageable)
                .map(partidaMapper::toResponseFull);
    }
}
