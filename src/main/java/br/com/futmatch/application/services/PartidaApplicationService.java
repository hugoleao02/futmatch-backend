package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.*;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.*;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaApplicationService implements
        CriarPartidaUseCase,
        AtualizarPartidaUseCase,
        ListarPartidasUseCase,
        BuscarPartidaPorIdUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PartidaMapper partidaMapper;

    @Override
    public PartidaResponse criarPartida(PartidaRequest request, Long criadorId) {
        validarDadosPartida(request);

        Usuario criador = buscarUsuarioPorId(criadorId);
        Partida partida = partidaMapper.toDomain(request);

        Participacao participacaoCriador = criarParticipacaoCriador(criador, partida);
        partida.adicionarParticipante(participacaoCriador);
        partida.setCriador(criador);

        Partida partidaSalva = partidaRepositoryPort.save(partida);
        return partidaMapper.toResponse(partidaSalva);
    }

    @Override
    public PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId) {
        Partida partida = buscarPartidaPorIdOuErro(id);
        validarPermissaoDoCriador(partida, usuarioId);

        validarDadosAtualizacao(request);
        aplicarAtualizacoesNaPartida(partida, request);

        Partida atualizada = partidaRepositoryPort.update(partida);
        return partidaMapper.toResponse(atualizada);
    }

    @Override
    public List<PartidaResponse> listarPartidas() {
        return partidaRepositoryPort.findAll().stream()
                .map(partidaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PartidaResponse buscarPartidaPorId(Long id) {
        return partidaMapper.toResponse(buscarPartidaPorIdOuErro(id));
    }


    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida não encontrada com ID: " + id));
    }

    private void validarPermissaoDoCriador(Partida partida, Long usuarioId) {
        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode atualizá-la");
        }
    }

    private Participacao criarParticipacaoCriador(Usuario criador, Partida partida) {
        return new Participacao(
                criador,
                partida,
                StatusParticipacao.CONFIRMADO,
                LocalDateTime.now()
        );
    }

    private void aplicarAtualizacoesNaPartida(Partida partida, PartidaUpdateRequest request) {
        if (request.getNome() != null) partida.setNome(request.getNome());
        if (request.getEsporte() != null) partida.setEsporte(Esporte.valueOf(request.getEsporte().toUpperCase()));
        if (request.getLatitude() != null) partida.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) partida.setLongitude(request.getLongitude());
        if (request.getDataHora() != null) partida.setDataHora(request.getDataHora());
        if (request.getTotalJogadores() != null) partida.setTotalJogadores(request.getTotalJogadores());
        if (request.getTipoPartida() != null) partida.setTipoPartida(TipoPartida.valueOf(request.getTipoPartida()
                .toUpperCase()));
    }

    private void validarDadosPartida(PartidaRequest request) {
        validarEnum(Esporte.class, request.getEsporte(), "Esporte inválido: ");
        validarEnum(TipoPartida.class, request.getTipoPartida(), "Tipo de partida inválido: ");
        validarLimiteJogadoresPorEsporte(request.getEsporte(), request.getTotalJogadores());
    }

    private void validarDadosAtualizacao(PartidaUpdateRequest request) {
        if (request.getEsporte() != null)
            validarEnum(Esporte.class, request.getEsporte(), "Esporte inválido: ");

        if (request.getTipoPartida() != null)
            validarEnum(TipoPartida.class, request.getTipoPartida(), "Tipo de partida inválido: ");

        validarLimiteJogadoresPorEsporte(request.getEsporte(), request.getTotalJogadores());
    }

    private <E extends Enum<E>> void validarEnum(Class<E> enumClass, String valor, String mensagemErro) {
        try {
            Enum.valueOf(enumClass, valor.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(mensagemErro + valor);
        }
    }

    private void validarLimiteJogadoresPorEsporte(String esporte, Integer totalJogadores) {
        if ("FUTSAL".equalsIgnoreCase(esporte) && totalJogadores != null && totalJogadores > 10) {
            throw new IllegalArgumentException("Futsal permite no máximo 10 jogadores");
        }
    }
}
