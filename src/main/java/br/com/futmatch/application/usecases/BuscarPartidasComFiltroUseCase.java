package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.requests.PartidaFiltroRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import org.springframework.data.domain.Page;

public interface BuscarPartidasComFiltroUseCase {
    Page<PartidaResponse> buscarPartidasComFiltro(PartidaFiltroRequest filtro);
} 