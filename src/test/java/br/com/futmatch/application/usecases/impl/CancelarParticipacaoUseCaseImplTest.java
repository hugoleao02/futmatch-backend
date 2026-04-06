package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarParticipacaoUseCaseImplTest {

    @Mock
    private ParticipacaoRepositoryPort participacaoRepositoryPort;

    private CancelarParticipacaoUseCaseImpl useCase;

    // Helpers

    private Usuario criarUsuario(Long id, String nome) {
        return new Usuario(id, nome, nome + "@email.com", "senha123");
    }

    private Partida criarPartida(Long id, Usuario criador) {
        return new Partida(id, "Partida Teste", Esporte.FUTEBOL, null,
                LocalDateTime.now().plusDays(1), 22, TipoPartida.PUBLICA, criador);
    }

    private Participacao criarParticipacao(Long id, Usuario usuario, Partida partida) {
        return new Participacao(id, usuario, partida, StatusParticipacao.CONFIRMADO, LocalDateTime.now());
    }

    @Test
    void shouldCancelarSuccessfully() {
        Long partidaId = 1L;
        Long usuarioId = 10L;
        Usuario criador = criarUsuario(99L, "Criador");
        Partida partida = criarPartida(partidaId, criador);
        Usuario participante = criarUsuario(usuarioId, "Participante");
        Participacao participacao = criarParticipacao(1L, participante, partida);

        when(participacaoRepositoryPort.findByUsuarioAndPartida(usuarioId, partidaId))
                .thenReturn(Optional.of(participacao));

        useCase = new CancelarParticipacaoUseCaseImpl(participacaoRepositoryPort);

        useCase.cancelarParticipacao(partidaId, usuarioId);

        verify(participacaoRepositoryPort).delete(participacao);
    }

    @Test
    void shouldThrowWhenNotFound() {
        Long partidaId = 1L;
        Long usuarioId = 10L;

        when(participacaoRepositoryPort.findByUsuarioAndPartida(usuarioId, partidaId))
                .thenReturn(Optional.empty());

        useCase = new CancelarParticipacaoUseCaseImpl(participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.cancelarParticipacao(partidaId, usuarioId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Participacao nao encontrada");
    }

    @Test
    void shouldThrowWhenNotPermitted() {
        Long partidaId = 1L;
        Long requestUserId = 10L;
        Usuario criador = criarUsuario(99L, "Criador");
        Partida partida = criarPartida(partidaId, criador);
        Usuario outroUsuario = criarUsuario(20L, "Outro");
        Participacao participacaoDoOutro = criarParticipacao(1L, outroUsuario, partida);

        // Mock returns a participacao belonging to outroUsuario when querying by requestUserId
        // This simulates the impl finding the record but the caller not being the owner or creator
        when(participacaoRepositoryPort.findByUsuarioAndPartida(requestUserId, partidaId))
                .thenReturn(Optional.of(participacaoDoOutro));

        useCase = new CancelarParticipacaoUseCaseImpl(participacaoRepositoryPort);

        // requestUserId=10 tries to cancel, but the participacao belongs to usuario=20,
        // and criador=99, so neither matches -> not permitted
        assertThatThrownBy(() -> useCase.cancelarParticipacao(partidaId, requestUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Apenas o participante ou o criador da partida pode cancelar a participacao");

        verify(participacaoRepositoryPort, never()).delete(any());
    }
}
