package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
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
import br.com.futmatch.domain.valueobjects.Localizacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticiparPartidaUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private ParticipacaoRepositoryPort participacaoRepositoryPort;

    private ParticiparPartidaUseCaseImpl useCase;

    private Usuario criarUsuario(Long id, String nome, String email) {
        return new Usuario(id, nome, email, "senha123");
    }

    private Partida criarPartida(Long id, Usuario criador, LocalDateTime dataHora, Integer totalJogadores) {
        return new Partida(id, "Partida Teste", Esporte.FUTEBOL,
                new Localizacao(-23.55, -46.63),
                dataHora,
                totalJogadores, TipoPartida.PUBLICA, criador);
    }

    @Test
    void shouldParticiparSuccessfully() {
        Long partidaId = 1L;
        Long usuarioId = 20L;
        Usuario criador = criarUsuario(10L, "Criador", "criador@email.com");
        Usuario jogador = criarUsuario(usuarioId, "Jogador", "jogador@email.com");

        Partida partida = criarPartida(partidaId, criador,
                LocalDateTime.now().plusDays(3), 10);
        Participacao participacao = new Participacao(jogador, partida,
                StatusParticipacao.PENDENTE, LocalDateTime.now());
        ParticipacaoResponse response = new ParticipacaoResponse();
        response.setId(1L);
        response.setUsuarioId(usuarioId);
        response.setPartidaId(partidaId);
        response.setStatus("PENDENTE");

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(usuarioRepositoryPort.findById(usuarioId)).thenReturn(Optional.of(jogador));
        when(participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partidaId)).thenReturn(false);
        when(participacaoRepositoryPort.countByPartidaAndStatus(partidaId, StatusParticipacao.CONFIRMADO)).thenReturn(3L);
        when(participacaoRepositoryPort.save(any(Participacao.class))).thenReturn(participacao);

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        ParticipacaoResponse result = useCase.participarPartida(partidaId, usuarioId);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("PENDENTE");
        verify(participacaoRepositoryPort).save(any(Participacao.class));
    }

    @Test
    void shouldThrowWhenPartidaNotFound() {
        Long partidaId = 99L;
        Long usuarioId = 20L;

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.empty());

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.participarPartida(partidaId, usuarioId))
                .isInstanceOf(PartidaNotFoundException.class)
                .hasMessageContaining("Partida nao encontrada");
    }

    @Test
    void shouldThrowWhenUsuarioNotFound() {
        Long partidaId = 1L;
        Long usuarioId = 99L;
        Usuario criador = criarUsuario(10L, "Criador", "criador@email.com");
        Partida partida = criarPartida(partidaId, criador,
                LocalDateTime.now().plusDays(3), 10);

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(usuarioRepositoryPort.findById(usuarioId)).thenReturn(Optional.empty());

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.participarPartida(partidaId, usuarioId))
                .isInstanceOf(UsuarioNotFoundException.class)
                .hasMessageContaining("Usuario nao encontrado");
    }

    @Test
    void shouldThrowWhenAlreadyParticipating() {
        Long partidaId = 1L;
        Long usuarioId = 20L;
        Usuario criador = criarUsuario(10L, "Criador", "criador@email.com");
        Partida partida = criarPartida(partidaId, criador,
                LocalDateTime.now().plusDays(3), 10);
        Usuario jogador = criarUsuario(usuarioId, "Jogador", "jogador@email.com");

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(usuarioRepositoryPort.findById(usuarioId)).thenReturn(Optional.of(jogador));
        when(participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partidaId)).thenReturn(true);

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.participarPartida(partidaId, usuarioId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuario ja participa desta partida");
    }

    @Test
    void shouldThrowWhenFull() {
        Long partidaId = 1L;
        Long usuarioId = 20L;
        Usuario criador = criarUsuario(10L, "Criador", "criador@email.com");
        Partida partida = criarPartida(partidaId, criador,
                LocalDateTime.now().plusDays(3), 5);
        Usuario jogador = criarUsuario(usuarioId, "Jogador", "jogador@email.com");

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(usuarioRepositoryPort.findById(usuarioId)).thenReturn(Optional.of(jogador));
        when(participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partidaId)).thenReturn(false);
        when(participacaoRepositoryPort.countByPartidaAndStatus(partidaId, StatusParticipacao.CONFIRMADO)).thenReturn(5L);

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.participarPartida(partidaId, usuarioId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("numero maximo de jogadores");
    }

    @Test
    void shouldThrowWhenPartidaJaRealizada() {
        Long partidaId = 1L;
        Long usuarioId = 20L;
        Usuario criador = criarUsuario(10L, "Criador", "criador@email.com");
        Partida partida = criarPartida(partidaId, criador,
                LocalDateTime.now().minusDays(1), 10);
        Usuario jogador = criarUsuario(usuarioId, "Jogador", "jogador@email.com");

        when(partidaRepositoryPort.findById(partidaId)).thenReturn(Optional.of(partida));
        when(usuarioRepositoryPort.findById(usuarioId)).thenReturn(Optional.of(jogador));
        when(participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partidaId)).thenReturn(false);
        when(participacaoRepositoryPort.countByPartidaAndStatus(partidaId, StatusParticipacao.CONFIRMADO)).thenReturn(0L);

        useCase = new ParticiparPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, participacaoRepositoryPort);

        assertThatThrownBy(() -> useCase.participarPartida(partidaId, usuarioId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ja aconteceu");
    }
}