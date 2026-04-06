package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPartidasUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private ListarPartidasUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarPartidasUseCaseImpl(partidaRepositoryPort, partidaMapper);
    }

    @Test
    void shouldReturnAllPartidas() {
        Usuario criador = new Usuario(1L, "Criador", "criador@test.com", "pass");
        Partida p1 = new Partida(1L, "Match1", null, null, LocalDateTime.now().plusDays(5), 10, null, criador);
        Partida p2 = new Partida(2L, "Match2", null, null, LocalDateTime.now().plusDays(6), 22, null, criador);

        PartidaResponse r1 = new PartidaResponse();
        r1.setId(1L);
        PartidaResponse r2 = new PartidaResponse();
        r2.setId(2L);

        when(partidaRepositoryPort.findAll()).thenReturn(Arrays.asList(p1, p2));
        when(partidaMapper.toResponse(p1)).thenReturn(r1);
        when(partidaMapper.toResponse(p2)).thenReturn(r2);

        List<PartidaResponse> result = useCase.listarPartidas();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void shouldReturnEmptyListWhenNoPartidas() {
        when(partidaRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        var result = useCase.listarPartidas();

        assertTrue(result.isEmpty());
    }
}
