package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;

public interface GerenciarParticipacaoUseCase {
    ParticipacaoResponse aprovarParticipacao(Long partidaId, Long participanteId, Long criadorId);
    ParticipacaoResponse rejeitarParticipacao(Long partidaId, Long participanteId, Long criadorId);
} 