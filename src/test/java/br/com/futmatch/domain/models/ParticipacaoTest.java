package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.valueobjects.Email;
import br.com.futmatch.domain.valueobjects.Localizacao;
import br.com.futmatch.domain.valueobjects.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ParticipacaoTest {

    private Usuario criador;
    private Partida partida;
    private Usuario participante;

    @BeforeEach
    void setUp() {
        criador = new Usuario("Criador", new Email("criador@test.com"), new Senha("Senha@123"));
        participante = new Usuario("Jogador", new Email("jogador@test.com"), new Senha("Senha@123"));

        LocalDateTime dataFutura = LocalDateTime.now().plusDays(5).withHour(14).withMinute(0);
        partida = new Partida("Test Match", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dataFutura, 10, TipoPartida.PUBLICA, criador);
    }

    @Test
    void shouldCreateParticipacaoWithUsuarioAndPartida() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.PENDENTE, LocalDateTime.now());

        assertEquals(participante, participacao.getUsuario());
        assertEquals(partida, participacao.getPartida());
        assertEquals(StatusParticipacao.PENDENTE, participacao.getStatus());
        assertNotNull(participacao.getDataParticipacao());
    }

    @Test
    void shouldConfirmParticipacao() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.PENDENTE, LocalDateTime.now());

        participacao.confirmar();

        assertEquals(StatusParticipacao.CONFIRMADO, participacao.getStatus());
    }

    @Test
    void shouldRejeitarParticipacao() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.PENDENTE, LocalDateTime.now());

        participacao.rejeitar();

        assertEquals(StatusParticipacao.REJEITADO, participacao.getStatus());
    }

    @Test
    void shouldCancelParticipacao() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.CONFIRMADO, LocalDateTime.now());

        participacao.cancelar();

        assertEquals(StatusParticipacao.CANCELADO, participacao.getStatus());
    }

    @Test
    void shouldNotConfirmAlreadyConfirmedParticipacao() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.CONFIRMADO, LocalDateTime.now());

        assertThrows(IllegalStateException.class, participacao::confirmar);
    }

    @Test
    void shouldNotCancelParticipacaoThatIsPendente() {
        Participacao participacao = new Participacao(participante, partida, StatusParticipacao.PENDENTE, LocalDateTime.now());

        assertThrows(IllegalStateException.class, participacao::cancelar);
    }
}
