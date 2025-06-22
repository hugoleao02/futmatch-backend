package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.PartidaResponse;

public interface BuscarPartidaPorIdUseCase {
    PartidaResponse buscarPartidaPorId(Long id);
} 