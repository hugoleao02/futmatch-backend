package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.usecases.CriarPartidaUseCase;
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
public class CriarPartidaUseCaseImpl implements CriarPartidaUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PartidaMapper partidaMapper;

    public CriarPartidaUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                   UsuarioRepositoryPort usuarioRepositoryPort,
                                   PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public PartidaResponse criarPartida(PartidaRequest request, Long criadorId) {
        validarDadosPartida(request);

        Usuario criador = usuarioRepositoryPort.findById(criadorId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario nao encontrado com ID: " + criadorId));

        Localizacao localizacao = new Localizacao(request.getLatitude(), request.getLongitude());

        Esporte esporte = Esporte.valueOf(request.getEsporte().toUpperCase());
        TipoPartida tipoPartida = TipoPartida.valueOf(request.getTipoPartida().toUpperCase());

        Partida partida = new Partida(
                request.getNome(),
                esporte,
                localizacao,
                request.getDataHora(),
                request.getTotalJogadores(),
                tipoPartida,
                criador
        );

        Partida partidaSalva = partidaRepositoryPort.save(partida);
        return partidaMapper.toResponseFull(partidaSalva);
    }

    private void validarDadosPartida(PartidaRequest request) {
        validarEnum(Esporte.class, request.getEsporte(), "Esporte invalido: ");
        validarEnum(TipoPartida.class, request.getTipoPartida(), "Tipo de partida invalido: ");
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
            throw new IllegalArgumentException("Futsal permite no maximo 10 jogadores");
        }
    }
}
