package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.PartidaDetalhesResponse;
import br.com.futmatch.application.dtos.responses.PartidaDetalhesResponse.ParticipanteInfo;
import br.com.futmatch.application.usecases.BuscarPartidaDetalhesUseCase;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.ParticipacaoSpringRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BuscarPartidaDetalhesUseCaseImpl implements BuscarPartidaDetalhesUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ParticipacaoSpringRepository participacaoSpringRepository;

    public BuscarPartidaDetalhesUseCaseImpl(PartidaRepositoryPort partidaRepositoryPort,
                                            UsuarioRepositoryPort usuarioRepositoryPort,
                                            ParticipacaoSpringRepository participacaoSpringRepository) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.participacaoSpringRepository = participacaoSpringRepository;
    }

    @Override
    public PartidaDetalhesResponse buscarDetalhes(Long partidaId, Long usuarioId) {
        Partida partida = partidaRepositoryPort.findById(partidaId)
                .orElseThrow(() -> new PartidaNotFoundException("Partida nao encontrada com ID: " + partidaId));

        Usuario usuario = usuarioRepositoryPort.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado com ID: " + usuarioId));

        List<ParticipacaoEntity> participantesConfirmados = participacaoSpringRepository
                .findByPartidaIdAndStatus(partidaId, StatusParticipacao.CONFIRMADO);

        List<ParticipacaoEntity> solicitacoesPendentes = participacaoSpringRepository
                .findByPartidaIdAndStatus(partidaId, StatusParticipacao.PENDENTE);

        long confirmadosCount = participantesConfirmados.size();

        boolean usuarioConfirmado = participantesConfirmados.stream()
                .anyMatch(p -> p.getUsuario().getId().equals(usuarioId));

        boolean hasSolicitacaoPendente = solicitacoesPendentes.stream()
                .anyMatch(p -> p.getUsuario().getId().equals(usuarioId));

        PartidaDetalhesResponse response = new PartidaDetalhesResponse();
        response.setId(partida.getId());
        response.setNome(partida.getNome());
        response.setEsporte(partida.getEsporte().name());
        response.setDataHora(partida.getDataHora());
        response.setTotalJogadores(partida.getTotalJogadores());
        response.setTipoPartida(partida.getTipoPartida().name());
        response.setParticipantesConfirmados((int) confirmadosCount);

        if (partida.getLocalizacao() != null) {
            response.setLatitude(partida.getLocalizacao().latitude());
            response.setLongitude(partida.getLocalizacao().longitude());
        }
        response.setNomeLocal(partida.getNomeLocal());

        if (partida.getCriador() != null) {
            response.setCriadorId(partida.getCriador().getId());
            response.setCriadorNome(partida.getCriador().getNome());
        }

        boolean usuarioEhCriador = partida.getCriador() != null
                && partida.getCriador().getId() != null
                && partida.getCriador().getId().equals(usuario.getId());
        response.setIsCriador(usuarioEhCriador);
        response.setIsParticipando(usuarioConfirmado);
        response.setHasSolicitado(hasSolicitacaoPendente);
        response.setTimes(null);

        response.setParticipantes(mapParaParticipanteInfo(participantesConfirmados));
        response.setSolicitacoes(mapParaParticipanteInfo(solicitacoesPendentes));

        return response;
    }

    private List<ParticipanteInfo> mapParaParticipanteInfo(List<ParticipacaoEntity> participacoes) {
        if (participacoes == null || participacoes.isEmpty()) {
            return Collections.emptyList();
        }
        return participacoes.stream().map(p -> {
            UsuarioEntity usuario = p.getUsuario();
            ParticipanteInfo info = new ParticipanteInfo();
            info.setId(usuario != null ? usuario.getId() : null);
            info.setNome(usuario != null ? usuario.getNome() : "Anonimo");
            info.setFotoPerfilUrl(usuario != null ? usuario.getFotoPerfilUrl() : null);
            info.setStatus(p.getStatus() != null ? p.getStatus().name() : null);
            return info;
        }).toList();
    }
}
