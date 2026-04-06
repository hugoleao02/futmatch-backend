package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirPartidaUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private ParticipacaoRepositoryPort participacaoRepositoryPort;

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private ExcluirPartidaUseCaseImpl useCase;

    // Helper methods

    private Usuario criarUsuario(Long id, String nome) {
        return new Usuario(id, nome, nome + "@email.com", "senha123");
    }

    private Partida criarPartida(Long id, Usuario criador) {
        return new Partida(id, "Partida Teste", Esporte.FUTEBOL, null,
                LocalDateTime.now().plusDays(1), 22, TipoPartida.PUBLICA, criador);
    }

    @Test
    void shouldExcluirSuccessfully() {
        Long partidaId = 1L;
        Long criadorId = 10L;
        Usuario criador = criarUsuario(criadorId, "Criador");
        Partida partida = criarPartida(partidaId, criador);

        Participacao participacao1 = new Participacao(1L, criador, partida, StatusParticipacao.CONFIRMADO, LocalDateTime.now());
        Participacao participacao2 = new Participacao(2L, criarUsuario(20L, "Jogador2"), partida, StatusParticipacao.PENDENTE, LocalDateTime.now());

        when(usuarioRepositoryPort.findById(criadorId)).thenReturn(Optional.of(criador));
        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(participacaoRepositoryPort.findByPartidaId(partidaId)).thenReturn(List.of(participacao1, participacao2));

        useCase = new ExcluirPartidaUseCaseImpl(partidaRepositoryPort, participacaoRepositoryPort, usuarioRepositoryPort);

        useCase.excluirPartida(partidaId, criadorId);

        verify(participacaoRepositoryPort).delete(participacao1);
        verify(participacaoRepositoryPort).delete(participacao2);
        verify(partidaRepositoryPort).delete(partida);
    }

    @Test
    void shouldThrowWhenNotOwner() {
        Long partidaId = 1L;
        Long criadorId = 10L;
        Long outroUsuarioId = 99L;
        Usuario criador = criarUsuario(criadorId, "Criador");
        Partida partida = criarPartida(partidaId, criador);

        when(usuarioRepositoryPort.findById(outroUsuarioId)).thenReturn(Optional.of(criarUsuario(outroUsuarioId, "Outro")));
        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));

        useCase = new ExcluirPartidaUseCaseImpl(partidaRepositoryPort, participacaoRepositoryPort, usuarioRepositoryPort);

        assertThatThrownBy(() -> useCase.excluirPartida(partidaId, outroUsuarioId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Apenas o criador da partida pode realizar esta acao");

        verify(participacaoRepositoryPort, never()).findByPartidaId(any());
        verify(partidaRepositoryPort, never()).delete(any());
    }

    @Test
    void shouldThrowWhenUsuarioNotFound() {
        Long partidaId = 1L;
        Long usuarioInexistenteId = 999L;

        when(usuarioRepositoryPort.findById(usuarioInexistenteId)).thenReturn(Optional.empty());

        useCase = new ExcluirPartidaUseCaseImpl(partidaRepositoryPort, participacaoRepositoryPort, usuarioRepositoryPort);

        assertThatThrownBy(() -> useCase.excluirPartida(partidaId, usuarioInexistenteId))
                .isInstanceOf(UsuarioNotFoundException.class)
                .hasMessageContaining("Usuario nao encontrado");

        verify(partidaRepositoryPort, never()).findById(any());
    }

    @Test
    void shouldThrowWhenPartidaNotFound() {
        Long partidaId = 1L;
        Long criadorId = 10L;
        Usuario criador = criarUsuario(criadorId, "Criador");

        when(usuarioRepositoryPort.findById(criadorId)).thenReturn(Optional.of(criador));
        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.empty());

        useCase = new ExcluirPartidaUseCaseImpl(partidaRepositoryPort, participacaoRepositoryPort, usuarioRepositoryPort);

        assertThatThrownBy(() -> useCase.excluirPartida(partidaId, criadorId))
                .isInstanceOf(PartidaNotFoundException.class)
                .hasMessageContaining("Partida nao encontrada");
    }
}
