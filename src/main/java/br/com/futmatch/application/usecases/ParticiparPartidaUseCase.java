package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;

public interface ParticiparPartidaUseCase {
    ParticipacaoResponse participarPartida(Long partidaId, Long usuarioId);
} 