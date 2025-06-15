package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.PartidaResponse;
import br.com.futmatch.application.dtos.PartidaUpdateRequest;

public interface AtualizarPartidaUseCase {
    PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId);
} 