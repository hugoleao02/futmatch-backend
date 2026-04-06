package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
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
class BuscarPartidaPorIdUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private BuscarPartidaPorIdUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarPartidaPorIdUseCaseImpl(partidaRepositoryPort, partidaMapper);
    }

    @Test
    void shouldReturnPartidaWhenFound() {
        Usuario criador = new Usuario(1L, "Criador", "criador@test.com", "Senha@123");
        Partida partida = new Partida(1L, "Test", null, null, LocalDateTime.now().plusDays(5), 10, null, criador);

        PartidaResponse response = new PartidaResponse();
        response.setId(1L);

        when(partidaRepositoryPort.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaMapper.toResponseFull(partida)).thenReturn(response);

        PartidaResponse result = useCase.buscarPartidaPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(partidaMapper).toResponseFull(partida);
    }

    @Test
    void shouldThrowWhenNotFound() {
        when(partidaRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PartidaNotFoundException.class, () -> useCase.buscarPartidaPorId(99L));
    }
}
