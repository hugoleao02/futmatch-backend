package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;

public interface AtualizarPartidaUseCase {
    PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId);
} 