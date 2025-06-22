package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.usecases.AtualizarPartidaUseCase;
import br.com.futmatch.application.usecases.BuscarPartidaPorIdUseCase;
import br.com.futmatch.application.usecases.CriarPartidaUseCase;
import br.com.futmatch.application.usecases.ListarPartidasUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.*;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.ParticipacaoMapper;
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

        Usuario criador = usuarioRepositoryPort.findById(criadorId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + criadorId));


        Partida partida = partidaMapper.toDomain(request);

        var participacaoCriador = new Participacao();
        participacaoCriador.setUsuario(criador);
        participacaoCriador.setPartida(partida);
        participacaoCriador.setStatus(StatusParticipacao.CONFIRMADO);
        participacaoCriador.setDataParticipacao(LocalDateTime.now());

        partida.adicionarParticipante(participacaoCriador);

        Partida partidaSalva = partidaRepositoryPort.save(partida);

        return partidaMapper.toResponse(partidaSalva);
    }
    
    private void validarDadosPartida(PartidaRequest request) {
        try {
            Esporte.valueOf(request.getEsporte().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Esporte inválido: " + request.getEsporte());
        }

        try {
            TipoPartida.valueOf(request.getTipoPartida().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de partida inválido: " + request.getTipoPartida());
        }
        
        // Validação adicional: mínimo de jogadores por esporte
        if (request.getEsporte().equalsIgnoreCase("FUTSAL") && request.getTotalJogadores() > 10) {
            throw new IllegalArgumentException("Futsal permite no máximo 10 jogadores");
        }
    }

    @Override
    public PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId) {
        Partida partida = partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida não encontrada com ID: " + id));

        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode atualizá-la");
        }

        validarDadosAtualizacao(request);

        if (request.getNome() != null) {
            partida.setNome(request.getNome());
        }
        if (request.getEsporte() != null) {
            partida.setEsporte(Esporte.valueOf(request.getEsporte().toUpperCase()));
        }
        if (request.getLatitude() != null) {
            partida.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            partida.setLongitude(request.getLongitude());
        }
        if (request.getDataHora() != null) {
            partida.setDataHora(request.getDataHora());
        }
        if (request.getTotalJogadores() != null) {
            partida.setTotalJogadores(request.getTotalJogadores());
        }
        if (request.getTipoPartida() != null) {
            partida.setTipoPartida(TipoPartida.valueOf(request.getTipoPartida().toUpperCase()));
        }

        Partida partidaAtualizada = partidaRepositoryPort.update(partida);

        return partidaMapper.toResponse(partidaAtualizada);
    }

    private void validarDadosAtualizacao(PartidaUpdateRequest request) {
        if (request.getEsporte() != null) {
            try {
                Esporte.valueOf(request.getEsporte().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Esporte inválido: " + request.getEsporte());
            }
        }

        if (request.getTipoPartida() != null) {
            try {
                TipoPartida.valueOf(request.getTipoPartida().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de partida inválido: " + request.getTipoPartida());
            }
        }

        if (request.getEsporte() != null && request.getTotalJogadores() != null) {
            if (request.getEsporte().equalsIgnoreCase("FUTSAL") && request.getTotalJogadores() > 10) {
                throw new IllegalArgumentException("Futsal permite no máximo 10 jogadores");
            }
        }
    }

    @Override
    public List<PartidaResponse> listarPartidas() {
        List<Partida> partidas = partidaRepositoryPort.findAll();
        return partidas.stream()
                .map(partidaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PartidaResponse buscarPartidaPorId(Long id) {
        Partida partida = partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida não encontrada com ID: " + id));
        return partidaMapper.toResponse(partida);
    }
} 