package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.application.usecases.ParticiparPartidaUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.ParticipacaoMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParticiparPartidaUseCaseImpl implements ParticiparPartidaUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ParticipacaoRepositoryPort participacaoRepositoryPort;
    private final ParticipacaoMapper participacaoMapper;

    public ParticiparPartidaUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                        UsuarioRepositoryPort usuarioRepositoryPort,
                                        ParticipacaoRepositoryPort participacaoRepositoryPort) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.participacaoRepositoryPort = participacaoRepositoryPort;
        this.participacaoMapper = new ParticipacaoMapper();
    }

    @Override
    public ParticipacaoResponse participarPartida(Long partidaId, Long usuarioId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario nao encontrado com ID: " + usuarioId));

        validarParticipacao(partida, usuarioId);

        Participacao participacao = new Participacao(
                usuario,
                partida,
                StatusParticipacao.PENDENTE,
                LocalDateTime.now()
        );

        Participacao participacaoSalva = participacaoRepositoryPort.save(participacao);
        return participacaoMapper.toResponse(participacaoSalva);
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + id));
    }

    private void validarParticipacao(Partida partida, Long usuarioId) {
        if (participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partida.getId())) {
            throw new IllegalArgumentException("Usuario ja participa desta partida");
        }

        long participantesConfirmados = participacaoRepositoryPort.countByPartidaAndStatus(partida.getId(), StatusParticipacao.CONFIRMADO);
        if (participantesConfirmados >= partida.getTotalJogadores()) {
            throw new IllegalArgumentException("Partida ja esta com o numero maximo de jogadores");
        }

        if (partida.isJogosJaRealizada()) {
            throw new IllegalArgumentException("Nao e possivel participar de uma partida que ja aconteceu");
        }
    }
}
