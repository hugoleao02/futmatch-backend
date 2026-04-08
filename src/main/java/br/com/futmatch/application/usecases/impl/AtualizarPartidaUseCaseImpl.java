package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.AtualizarPartidaUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.domain.valueobjects.Localizacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.stereotype.Service;

@Service
public class AtualizarPartidaUseCaseImpl implements AtualizarPartidaUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PartidaMapper partidaMapper;

    public AtualizarPartidaUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                       UsuarioRepositoryPort usuarioRepositoryPort,
                                       PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId) {
        Partida partida = buscarPartidaPorIdOuErro(id);
        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario nao encontrado com ID: " + usuarioId));
        partida.validarAtualizacaoPor(usuario);

        validarDadosAtualizacao(request);
        aplicarAtualizacoesNaPartida(partida, request);

        Partida atualizada = partidaRepositoryPort.update(partida);
        return partidaMapper.toResponseFull(atualizada);
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + id));
    }

    private void validarDadosAtualizacao(PartidaUpdateRequest request) {
        if (request.getEsporte() != null) {
            validarEnum(Esporte.class, request.getEsporte(), "Esporte invalido: ");
        }
        if (request.getTipoPartida() != null) {
            validarEnum(TipoPartida.class, request.getTipoPartida(), "Tipo de partida invalido: ");
        }
        if (request.getEsporte() != null && request.getTotalJogadores() != null) {
            validarLimiteJogadoresPorEsporte(request.getEsporte(), request.getTotalJogadores());
        }
    }

    private void aplicarAtualizacoesNaPartida(Partida partida, PartidaUpdateRequest request) {
        if (request.getNome() != null) partida.atualizarNome(request.getNome());
        if (request.getEsporte() != null) partida.atualizarEsporte(Esporte.valueOf(request.getEsporte().toUpperCase()));
        if (request.getLatitude() != null && request.getLongitude() != null) {
            partida.atualizarLocalizacao(new Localizacao(request.getLatitude(), request.getLongitude()));
        }
        if (request.getDataHora() != null) partida.atualizarDataHora(request.getDataHora());
        if (request.getTotalJogadores() != null) partida.atualizarTotalJogadores(request.getTotalJogadores());
        if (request.getTipoPartida() != null) partida.atualizarTipoPartida(TipoPartida.valueOf(request.getTipoPartida().toUpperCase()));
        if (request.getNomeLocal() != null) partida.atualizarNomeLocal(request.getNomeLocal());
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
            throw new IllegalArgumentException("Futsal permite no maximo 10 jogadores");
        }
    }
}
