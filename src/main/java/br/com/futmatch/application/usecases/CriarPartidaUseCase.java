package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.PartidaRequest;
import br.com.futmatch.application.dtos.PartidaResponse;

public interface CriarPartidaUseCase {
    PartidaResponse criarPartida(PartidaRequest request, Long criadorId);
} 