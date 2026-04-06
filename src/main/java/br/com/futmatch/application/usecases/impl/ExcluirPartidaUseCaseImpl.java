package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.usecases.ExcluirPartidaUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcluirPartidaUseCaseImpl implements ExcluirPartidaUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final ParticipacaoRepositoryPort participacaoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ExcluirPartidaUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                     ParticipacaoRepositoryPort participacaoRepositoryPort,
                                     UsuarioRepositoryPort usuarioRepositoryPort) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.participacaoRepositoryPort = participacaoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public void excluirPartida(Long partidaId, Long criadorId) {
        // verify creator exists
        usuarioRepositoryPort.findById(criadorId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario nao encontrado com ID: " + criadorId));

        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        // Delete all participacoes
        List<Participacao> participacoes = participacaoRepositoryPort.findByPartidaId(partidaId);
        participacoes.forEach(participacaoRepositoryPort::delete);

        // Delete the match
        partidaRepositoryPort.delete(partida);
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + id));
    }

    private void validaPermissaoDoCriador(Partida partida, Long usuarioId) {
        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode realizar esta acao");
        }
    }
}
