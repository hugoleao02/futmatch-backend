package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;

public interface CriarPartidaUseCase {
    PartidaResponse criarPartida(PartidaRequest request, Long criadorId);
} 