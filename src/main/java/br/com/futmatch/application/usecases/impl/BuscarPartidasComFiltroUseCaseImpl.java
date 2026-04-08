package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaFiltroRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.BuscarPartidasComFiltroUseCase;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BuscarPartidasComFiltroUseCaseImpl implements BuscarPartidasComFiltroUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final PartidaMapper partidaMapper;

    public BuscarPartidasComFiltroUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                              PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public Page<PartidaResponse> buscarPartidasComFiltro(PartidaFiltroRequest filtro) {
        Pageable pageable = getPageable(filtro);

        if (filtro.getEsporte() != null && filtro.getTipoPartida() != null) {
            return partidaRepositoryPort.findByEsporteAndTipoPartida(filtro.getEsporte(), filtro.getTipoPartida(), pageable)
                    .map(partidaMapper::toResponseFull);
        } else if (filtro.getEsporte() != null) {
            return partidaRepositoryPort.findByEsporte(filtro.getEsporte(), pageable)
                    .map(partidaMapper::toResponseFull);
        } else if (filtro.getTipoPartida() != null) {
            return partidaRepositoryPort.findByTipoPartida(filtro.getTipoPartida(), pageable)
                    .map(partidaMapper::toResponseFull);
        } else if (filtro.getApenasFuturas() != null && filtro.getApenasFuturas()) {
            return partidaRepositoryPort.findAllFuturas(pageable)
                    .map(partidaMapper::toResponseFull);
        }
        return partidaRepositoryPort.findAllFuturas(pageable)
                .map(partidaMapper::toResponseFull);
    }

    private Pageable getPageable(PartidaFiltroRequest filtro) {
        int page = filtro.getPage() != null ? filtro.getPage() : 0;
        int size = filtro.getSize() != null ? filtro.getSize() : 10;
        return Pageable.ofSize(size).withPage(page);
    }
}
