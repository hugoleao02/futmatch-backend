package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurgarPartidasEncerradasUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private ParticipacaoRepositoryPort participacaoRepositoryPort;

    private PurgarPartidasEncerradasUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new PurgarPartidasEncerradasUseCaseImpl(partidaRepositoryPort, participacaoRepositoryPort, 1);
    }

    @Test
    void deveRetornarZeroQuandoNaoHaCandidatas() {
        when(partidaRepositoryPort.findByDataHoraBefore(any())).thenReturn(List.of());

        assertThat(useCase.executar()).isZero();

        verify(partidaRepositoryPort).findByDataHoraBefore(any());
        verifyNoMoreInteractions(participacaoRepositoryPort);
    }

    @Test
    void deveExcluirParticipacoesEPartida() {
        Usuario criador = new Usuario(10L, "Criador", "c@mail.com", "x");
        Partida partida = new Partida(5L, "Velha", Esporte.FUTEBOL, null,
                LocalDateTime.now().minusDays(5), 10, TipoPartida.PUBLICA, criador);
        Participacao pa = new Participacao(1L, criador, partida, StatusParticipacao.CONFIRMADO, LocalDateTime.now());

        when(partidaRepositoryPort.findByDataHoraBefore(any())).thenReturn(List.of(partida));
        when(participacaoRepositoryPort.findByPartidaId(5L)).thenReturn(List.of(pa));

        assertThat(useCase.executar()).isEqualTo(1);

        verify(participacaoRepositoryPort).delete(pa);
        verify(partidaRepositoryPort).delete(partida);
    }
}
