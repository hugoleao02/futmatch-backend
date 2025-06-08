package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.PartidaRequest;
import br.com.futmatch.application.dtos.PartidaResponse;
import br.com.futmatch.application.dtos.PartidaUpdateRequest;
import br.com.futmatch.application.usecases.AtualizarPartidaUseCase;
import br.com.futmatch.application.usecases.CriarPartidaUseCase;
import br.com.futmatch.application.usecases.ListarPartidasUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.*;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidaApplicationService implements CriarPartidaUseCase, AtualizarPartidaUseCase, ListarPartidasUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public PartidaResponse criarPartida(PartidaRequest request, Long criadorId) {
        // Validações de negócio adicionais
        validarDadosPartida(request);
        
        // Buscar o usuário criador
        Usuario criador = usuarioRepositoryPort.findById(criadorId)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + criadorId));
        
        Partida partida = Partida.builder()
                .nome(request.getNome())
                .esporte(Esporte.valueOf(request.getEsporte().toUpperCase()))
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .dataHora(request.getDataHora())
                .totalJogadores(request.getTotalJogadores())
                .tipoPartida(TipoPartida.valueOf(request.getTipoPartida().toUpperCase()))
                .criador(criador)
                .build();
        
        // Adicionar o criador como participante confirmado
        Participacao participacaoCriador = Participacao.builder()
                .usuario(criador)
                .partida(partida)
                .status(StatusParticipacao.CONFIRMADO)
                .dataParticipacao(LocalDateTime.now())
                .build();
        
        partida.adicionarParticipante(participacaoCriador);
        
        // Salvar a partida
        Partida partidaSalva = partidaRepositoryPort.save(partida);
        
        // Mapear para response
        return mapearParaResponse(partidaSalva);
    }
    
    private void validarDadosPartida(PartidaRequest request) {
        // Validar enum Esporte
        try {
            Esporte.valueOf(request.getEsporte().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Esporte inválido: " + request.getEsporte());
        }
        
        // Validar enum TipoPartida
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
    
    private PartidaResponse mapearParaResponse(Partida partida) {
        long participantesConfirmados = partida.getParticipantes().stream()
                .filter(p -> p.getStatus() == StatusParticipacao.CONFIRMADO)
                .count();
        
        return PartidaResponse.builder()
                .id(partida.getId())
                .nome(partida.getNome())
                .esporte(partida.getEsporte().name())
                .latitude(partida.getLatitude())
                .longitude(partida.getLongitude())
                .dataHora(partida.getDataHora())
                .totalJogadores(partida.getTotalJogadores())
                .tipoPartida(partida.getTipoPartida().name())
                .criadorId(partida.getCriador().getId())
                .criadorNome(partida.getCriador().getNome())
                .participantesConfirmados((int) participantesConfirmados)
                .build();
    }

    @Override
    public PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId) {
        // Buscar a partida existente
        Partida partida = partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida não encontrada com ID: " + id));

        // Verificar se o usuário é o criador da partida
        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode atualizá-la");
        }

        // Validar dados da atualização
        validarDadosAtualizacao(request);

        // Atualizar campos não nulos
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

        // Salvar atualizações
        Partida partidaAtualizada = partidaRepositoryPort.update(partida);

        // Mapear para response
        return mapearParaResponse(partidaAtualizada);
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
                .map(this::mapearParaResponse)
                .collect(Collectors.toList());
    }
} 