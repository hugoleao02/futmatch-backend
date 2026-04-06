package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.application.usecases.GerenciarParticipacaoUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.ParticipacaoMapper;
import org.springframework.stereotype.Service;

@Service
public class GerenciarParticipacaoUseCaseImpl implements GerenciarParticipacaoUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final ParticipacaoRepositoryPort participacaoRepositoryPort;
    private final ParticipacaoMapper participacaoMapper;

    public GerenciarParticipacaoUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                            ParticipacaoRepositoryPort participacaoRepositoryPort) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.participacaoRepositoryPort = participacaoRepositoryPort;
        this.participacaoMapper = new ParticipacaoMapper();
    }

    @Override
    public ParticipacaoResponse aprovarParticipacao(Long partidaId, Long participanteId, Long criadorId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        Participacao participacao = buscarParticipacaoOuErro(partidaId, participanteId);
        validarAprovacaoParticipacao(partida, participacao);

        participacao.confirmar();
        Participacao participacaoAtualizada = participacaoRepositoryPort.save(participacao);

        return participacaoMapper.toResponse(participacaoAtualizada);
    }

    @Override
    public ParticipacaoResponse rejeitarParticipacao(Long partidaId, Long participanteId, Long criadorId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        Participacao participacao = buscarParticipacaoOuErro(partidaId, participanteId);
        participacao.rejeitar();
        participacaoRepositoryPort.save(participacao);

        return participacaoMapper.toResponse(participacao);
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + id));
    }

    private Participacao buscarParticipacaoOuErro(Long partidaId, Long usuarioId) {
        return participacaoRepositoryPort.findByUsuarioAndPartida(usuarioId, partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Participacao nao encontrada"));
    }

    private void validaPermissaoDoCriador(Partida partida, Long usuarioId) {
        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode realizar esta acao");
        }
    }

    private void validarAprovacaoParticipacao(Partida partida, Participacao participacao) {
        if (participacao.getStatus() != StatusParticipacao.PENDENTE) {
            throw new IllegalArgumentException("Apenas participacoes pendentes podem ser aprovadas");
        }

        long participantesConfirmados = participacaoRepositoryPort.countByPartidaAndStatus(partida.getId(), StatusParticipacao.CONFIRMADO);
        if (participantesConfirmados >= partida.getTotalJogadores()) {
            throw new IllegalArgumentException("Partida ja esta com o numero maximo de jogadores");
        }
    }
}
