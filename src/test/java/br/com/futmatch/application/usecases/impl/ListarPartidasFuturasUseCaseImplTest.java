package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPartidasFuturasUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private ListarPartidasFuturasUseCaseImpl useCase;

    // Helpers

    private Usuario criarUsuario(Long id, String nome) {
        return new Usuario(id, nome, nome + "@email.com", "senha123");
    }

    private Partida criarPartida(Long id, Usuario criador) {
        return new Partida(id, "Partida Teste", Esporte.FUTEBOL, null,
                LocalDateTime.now().plusDays(1), 22, TipoPartida.PUBLICA, criador);
    }

    private PartidaResponse criarResponse(Long id) {
        PartidaResponse response = new PartidaResponse();
        response.setId(id);
        response.setNome("Partida Teste");
        response.setEsporte("FUTEBOL");
        response.setDataHora(LocalDateTime.now().plusDays(1));
        return response;
    }

    @Test
    void shouldReturnFuturasPartidas() {
        Usuario criador = criarUsuario(1L, "Criador");
        Partida partida1 = criarPartida(1L, criador);
        Partida partida2 = criarPartida(2L, criador);

        PartidaResponse response1 = criarResponse(1L);
        PartidaResponse response2 = criarResponse(2L);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida1, partida2), pageable, 2);
        Page<PartidaResponse> responsePage = new PageImpl<>(List.of(response1, response2), pageable, 2);

        when(partidaRepositoryPort.findAllFuturas(pageable)).thenReturn(partidaPage);
        when(partidaMapper.toResponseFull(partida1)).thenReturn(response1);
        when(partidaMapper.toResponseFull(partida2)).thenReturn(response2);

        useCase = new ListarPartidasFuturasUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.listarPartidasFuturas(pageable);

        assertThat(result).hasSize(2);
        assertThat(result.getContent()).containsExactly(response1, response2);
        verify(partidaRepositoryPort).findAllFuturas(pageable);
        verify(partidaMapper).toResponseFull(partida1);
        verify(partidaMapper).toResponseFull(partida2);
    }

    @Test
    void shouldReturnEmptyPageWhenNoneFuturas() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> emptyPage = Page.empty(pageable);

        when(partidaRepositoryPort.findAllFuturas(pageable)).thenReturn(emptyPage);

        useCase = new ListarPartidasFuturasUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.listarPartidasFuturas(pageable);

        assertThat(result).isEmpty();
        verify(partidaRepositoryPort).findAllFuturas(pageable);
        verify(partidaMapper, never()).toResponseFull(any());
    }
}
