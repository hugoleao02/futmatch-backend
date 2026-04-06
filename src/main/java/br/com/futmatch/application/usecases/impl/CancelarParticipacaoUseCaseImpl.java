package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.usecases.CancelarParticipacaoUseCase;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class CancelarParticipacaoUseCaseImpl implements CancelarParticipacaoUseCase {

    private final ParticipacaoRepositoryPort participacaoRepositoryPort;

    public CancelarParticipacaoUseCaseImpl(ParticipacaoRepositoryPort participacaoRepositoryPort) {
        this.participacaoRepositoryPort = participacaoRepositoryPort;
    }

    @Override
    public void cancelarParticipacao(Long partidaId, Long usuarioId) {
        Participacao participacao = buscarParticipacaoOuErro(partidaId, usuarioId);

        Usuario usuario = participacao.getUsuario();
        Usuario criador = participacao.getPartida().getCriador();
        if (!usuario.getId().equals(usuarioId) && !criador.getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o participante ou o criador da partida pode cancelar a participacao");
        }

        participacaoRepositoryPort.delete(participacao);
    }

    private Participacao buscarParticipacaoOuErro(Long partidaId, Long usuarioId) {
        return participacaoRepositoryPort.findByUsuarioAndPartida(usuarioId, partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Participacao nao encontrada"));
    }
}
