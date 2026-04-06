package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPartidaUseCaseImplTest {

    @Mock
    private PartidaRepositoryPort partidaRepositoryPort;

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PartidaMapper partidaMapper;

    private CriarPartidaUseCaseImpl useCase;

    @Test
    void shouldCreatePartidaSuccessfully() {
        Long criadorId = 1L;
        Usuario criador = new Usuario(criadorId, "Joao", "joao@email.com", "senha123");
        PartidaRequest request = buildValidRequest();
        Partida partida = new Partida(
                request.getNome(),
                br.com.futmatch.domain.models.enums.Esporte.FUTEBOL,
                new br.com.futmatch.domain.valueobjects.Localizacao(request.getLatitude(), request.getLongitude()),
                request.getDataHora(),
                request.getTotalJogadores(),
                br.com.futmatch.domain.models.enums.TipoPartida.PUBLICA,
                criador
        );
        Partida partidaSalva = new Partida(100L, request.getNome(),
                br.com.futmatch.domain.models.enums.Esporte.FUTEBOL,
                new br.com.futmatch.domain.valueobjects.Localizacao(request.getLatitude(), request.getLongitude()),
                request.getDataHora(), request.getTotalJogadores(),
                br.com.futmatch.domain.models.enums.TipoPartida.PUBLICA, criador);
        PartidaResponse response = new PartidaResponse();
        response.setId(partidaSalva.getId());
        response.setNome(partidaSalva.getNome());

        when(usuarioRepositoryPort.findById(criadorId)).thenReturn(Optional.of(criador));
        when(partidaRepositoryPort.save(any(Partida.class))).thenReturn(partidaSalva);
        when(partidaMapper.toResponseFull(partidaSalva)).thenReturn(response);

        useCase = new CriarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);

        PartidaResponse result = useCase.criarPartida(request, criadorId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(partidaSalva.getId());
        assertThat(result.getNome()).isEqualTo(partidaSalva.getNome());
        verify(usuarioRepositoryPort).findById(criadorId);
        verify(partidaRepositoryPort).save(any(Partida.class));
        verify(partidaMapper).toResponseFull(partidaSalva);
    }

    @Test
    void shouldThrowWhenCriadorNotFound() {
        Long criadorId = 99L;
        PartidaRequest request = buildValidRequest();

        when(usuarioRepositoryPort.findById(criadorId)).thenReturn(Optional.empty());

        useCase = new CriarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);

        assertThatThrownBy(() -> useCase.criarPartida(request, criadorId))
                .isInstanceOf(UsuarioNotFoundException.class)
                .hasMessageContaining("Usuario nao encontrado");

        verify(partidaRepositoryPort, never()).save(any());
        verify(partidaMapper, never()).toResponseFull(any());
    }

    @Test
    void shouldThrowWhenInvalidEsporte() {
        Long criadorId = 1L;
        PartidaRequest request = buildValidRequest();
        request.setEsporte("INVALID_SPORT");

        useCase = new CriarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);

        assertThatThrownBy(() -> useCase.criarPartida(request, criadorId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Esporte invalido");

        verify(usuarioRepositoryPort, never()).findById(any());
        verify(partidaRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenInvalidTipoPartida() {
        Long criadorId = 1L;
        PartidaRequest request = buildValidRequest();
        request.setTipoPartida("INVALID_TYPE");

        useCase = new CriarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);

        assertThatThrownBy(() -> useCase.criarPartida(request, criadorId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Tipo de partida invalido");

        verify(usuarioRepositoryPort, never()).findById(any());
        verify(partidaRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenFutsalExceedsLimit() {
        Long criadorId = 1L;
        PartidaRequest request = buildValidRequest();
        request.setEsporte("FUTSAL");
        request.setTotalJogadores(11);

        useCase = new CriarPartidaUseCaseImpl(partidaRepositoryPort, usuarioRepositoryPort, partidaMapper);

        assertThatThrownBy(() -> useCase.criarPartida(request, criadorId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Futsal permite no maximo 10 jogadores");

        verify(usuarioRepositoryPort, never()).findById(any());
        verify(partidaRepositoryPort, never()).save(any());
    }

    private PartidaRequest buildValidRequest() {
        PartidaRequest request = new PartidaRequest();
        request.setNome("Partida Teste");
        request.setEsporte("FUTEBOL");
        request.setLatitude(-23.55);
        request.setLongitude(-46.63);
        request.setDataHora(LocalDateTime.now().plusDays(1));
        request.setTotalJogadores(22);
        request.setTipoPartida("PUBLICA");
        return request;
    }
}
