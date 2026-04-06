package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.valueobjects.Email;
import br.com.futmatch.domain.valueobjects.Localizacao;
import br.com.futmatch.domain.valueobjects.Senha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private Usuario criador;
    private Usuario jogador1;
    private Usuario jogador2;

    @BeforeEach
    void setUp() {
        criador = new Usuario("Criador", new Email("criador@test.com"), new Senha("Senha@123"));
        jogador1 = new Usuario("Jogador1", new Email("j1@test.com"), new Senha("Senha@123"));
        jogador2 = new Usuario("Jogador2", new Email("j2@test.com"), new Senha("Senha@123"));
    }

    private LocalDateTime dadosFutura() {
        return LocalDateTime.now().plusDays(5).withHour(14).withMinute(0);
    }

    @Test
    void shouldCreatePartidaWithCriador() {
        Partida partida = new Partida("Test Match", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        assertEquals("Test Match", partida.getNome());
        assertEquals(Esporte.FUTEBOL, partida.getEsporte());
        assertEquals(criador, partida.getCriador());
        assertEquals(10, partida.getTotalJogadores());
        assertFalse(partida.estaCheia());
    }

    @Test
    void shouldAddParticipanteWhenCriadorCreates() {
        Partida partida = new Partida("Test", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        assertEquals(1, partida.totalParticipantes());
        assertTrue(partida.temParticipacaoDo(criador));
    }

    @Test
    void shouldNotBeFullWhenBelowLimit() {
        Partida partida = new Partida("Test", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        assertFalse(partida.estaCheia());
    }

    @Test
    void shouldNotBeFullWhenAtLimit() {
        Partida partida = new Partida("Test", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 1, TipoPartida.PUBLICA, criador);

        assertTrue(partida.estaCheia());
    }

    @Test
    void shouldUpdateNome() {
        Partida partida = new Partida("Old Name", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        partida.atualizarNome("New Name");

        assertEquals("New Name", partida.getNome());
    }

    @Test
    void shouldActualizarLocalizacao() {
        Partida partida = new Partida("Test", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        Localizacao nova = Localizacao.criadaPara(-22.90, -43.17);
        partida.atualizarLocalizacao(nova);

        assertEquals(nova, partida.getLocalizacao());
    }

    @Test
    void shouldValidateCriadorPermission() {
        Partida partida = new Partida("Test", Esporte.FUTEBOL, Localizacao.criadaPara(-23.55, -46.63),
            dadosFutura(), 10, TipoPartida.PUBLICA, criador);

        assertTrue(partida.ehCriadoPor(criador));
        assertFalse(partida.ehCriadoPor(jogador1));
    }

    @Test
    void shouldValidateFutsalPlayerLimit() {
        assertEquals(10, Esporte.FUTSAL.totalMaximoJogadores());
        assertEquals(22, Esporte.FUTEBOL.totalMaximoJogadores());
    }
}
