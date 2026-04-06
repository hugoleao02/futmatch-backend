package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.domain.valueobjects.Localizacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarPartidaUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private AtualizarPartidaUseCaseImpl useCase;

    private Usuario criador;
    private Partida partida;
    private Localizacao localizacao;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);
        localizacao = Localizacao.criadaPara(-23.55, -46.63);
        criador = new Usuario(1L, "Criador", "criador@test.com", "pass");
        partida = new Partida(1L, "Old", Esporte.FUTEBOL, localizacao, LocalDateTime.now().plusDays(5), 10, TipoPartida.PUBLICA, criador);
    }

    @Test
    void shouldUpdatePartidaSuccessfully() {
        PartidaUpdateRequest request = new PartidaUpdateRequest();
        request.setNome("Updated Name");

        when(partidaRepositoryPort.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepositoryPort.update(partida)).thenReturn(partida);

        PartidaResponse response = new PartidaResponse();
        response.setNome("Updated Name");

        when(partidaMapper.toResponseFull(partida)).thenReturn(response);

        PartidaResponse result = useCase.atualizarPartida(1L, request, 1L);

        assertEquals("Updated Name", partida.getNome());
        assertEquals("Updated Name", result.getNome());
    }

    @Test
    void shouldThrowWhenPartidaNotFound() {
        PartidaUpdateRequest request = new PartidaUpdateRequest();
        when(partidaRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PartidaNotFoundException.class, () -> useCase.atualizarPartida(99L, request, 1L));
    }

    @Test
    void shouldThrowWhenNotOwner() {
        Usuario outroUsuario = new Usuario(2L, "Other", "other@test.com", "pass");

        PartidaUpdateRequest request = new PartidaUpdateRequest();
        when(partidaRepositoryPort.findById(1L)).thenReturn(Optional.of(partida));

        assertThrows(IllegalArgumentException.class, () -> useCase.atualizarPartida(1L, request, 2L));
    }
}
