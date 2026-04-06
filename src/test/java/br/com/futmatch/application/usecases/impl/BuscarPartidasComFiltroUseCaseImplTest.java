package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaFiltroRequest;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarPartidasComFiltroUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private BuscarPartidasComFiltroUseCaseImpl useCase;

    // Helpers

    private Usuario criarUsuario(Long id, String nome) {
        return new Usuario(id, nome, nome + "@email.com", "senha123");
    }

    private Partida criarPartida(Long id, String nome, Usuario criador) {
        return new Partida(id, nome, Esporte.FUTEBOL, null,
                LocalDateTime.now().plusDays(1), 22, TipoPartida.PUBLICA, criador);
    }

    private Partida criarPartida(Long id) {
        return criarPartida(id, "Partida Teste", criarUsuario(1L, "Criador"));
    }

    private PartidaResponse criarResponse(Long id) {
        PartidaResponse response = new PartidaResponse();
        response.setId(id);
        response.setNome("Partida Teste");
        response.setEsporte("FUTEBOL");
        response.setDataHora(LocalDateTime.now().plusDays(1));
        response.setTipoPartida("PUBLICA");
        return response;
    }

    // Tests

    @Test
    void shouldFilterByEsporte() {
        Partida partida = criarPartida(1L, "Futebol Partida", criarUsuario(1L, "Criador"));
        PartidaResponse response = criarResponse(1L);

        PartidaFiltroRequest filtro = new PartidaFiltroRequest();
        filtro.setEsporte("FUTEBOL");
        filtro.setPage(0);
        filtro.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida), pageable, 1);
        Page<PartidaResponse> responsePage = new PageImpl<>(List.of(response), pageable, 1);

        when(partidaRepositoryPort.findByEsporte(eq("FUTEBOL"), eq(pageable))).thenReturn(partidaPage);
        when(partidaMapper.toResponse(partida)).thenReturn(response);

        useCase = new BuscarPartidasComFiltroUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.buscarPartidasComFiltro(filtro);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
        verify(partidaRepositoryPort).findByEsporte(eq("FUTEBOL"), eq(pageable));
    }

    @Test
    void shouldFilterByTipoPartida() {
        Partida partida = criarPartida(1L);
        PartidaResponse response = criarResponse(1L);

        PartidaFiltroRequest filtro = new PartidaFiltroRequest();
        filtro.setTipoPartida("PUBLICA");
        filtro.setPage(0);
        filtro.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida), pageable, 1);
        Page<PartidaResponse> responsePage = new PageImpl<>(List.of(response), pageable, 1);

        when(partidaRepositoryPort.findByTipoPartida(eq("PUBLICA"), eq(pageable))).thenReturn(partidaPage);
        when(partidaMapper.toResponse(partida)).thenReturn(response);

        useCase = new BuscarPartidasComFiltroUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.buscarPartidasComFiltro(filtro);

        assertThat(result).hasSize(1);
        verify(partidaRepositoryPort).findByTipoPartida(eq("PUBLICA"), eq(pageable));
    }

    @Test
    void shouldFilterByEsporteAndTipoPartida() {
        Partida partida = criarPartida(1L);
        PartidaResponse response = criarResponse(1L);

        PartidaFiltroRequest filtro = new PartidaFiltroRequest();
        filtro.setEsporte("FUTEBOL");
        filtro.setTipoPartida("PUBLICA");
        filtro.setPage(0);
        filtro.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida), pageable, 1);

        when(partidaRepositoryPort.findByEsporteAndTipoPartida(eq("FUTEBOL"), eq("PUBLICA"), eq(pageable)))
                .thenReturn(partidaPage);
        when(partidaMapper.toResponse(partida)).thenReturn(response);

        useCase = new BuscarPartidasComFiltroUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.buscarPartidasComFiltro(filtro);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
        verify(partidaRepositoryPort).findByEsporteAndTipoPartida(eq("FUTEBOL"), eq("PUBLICA"), eq(pageable));
    }

    @Test
    void shouldFilterFuturas() {
        Partida partida = criarPartida(1L);
        PartidaResponse response = criarResponse(1L);

        PartidaFiltroRequest filtro = new PartidaFiltroRequest();
        filtro.setApenasFuturas(true);
        filtro.setPage(0);
        filtro.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida), pageable, 1);

        when(partidaRepositoryPort.findAllFuturas(eq(pageable))).thenReturn(partidaPage);
        when(partidaMapper.toResponse(partida)).thenReturn(response);

        useCase = new BuscarPartidasComFiltroUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.buscarPartidasComFiltro(filtro);

        assertThat(result).hasSize(1);
        assertThat(result.getContent()).containsExactly(response);
        verify(partidaRepositoryPort).findAllFuturas(eq(pageable));
    }

    @Test
    void shouldReturnFuturasWhenNoFiltersProvided() {
        Partida partida = criarPartida(1L);
        PartidaResponse response = criarResponse(1L);

        PartidaFiltroRequest filtro = new PartidaFiltroRequest();
        filtro.setPage(0);
        filtro.setSize(10);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<Partida> partidaPage = new PageImpl<>(List.of(partida), pageable, 1);

        when(partidaRepositoryPort.findAllFuturas(eq(pageable))).thenReturn(partidaPage);
        when(partidaMapper.toResponse(partida)).thenReturn(response);

        useCase = new BuscarPartidasComFiltroUseCaseImpl(partidaRepositoryPort, partidaMapper);

        Page<PartidaResponse> result = useCase.buscarPartidasComFiltro(filtro);

        assertThat(result).hasSize(1);
        verify(partidaRepositoryPort).findAllFuturas(eq(pageable));
    }
}
