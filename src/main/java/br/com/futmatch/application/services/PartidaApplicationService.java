package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.application.usecases.*;
import br.com.futmatch.domain.exception.PartidaNotFoundException;
import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.*;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.domain.valueobjects.Localizacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.ParticipacaoMapper;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartidaApplicationService implements
        CriarPartidaUseCase,
        AtualizarPartidaUseCase,
        ListarPartidasUseCase,
        BuscarPartidaPorIdUseCase,
        BuscarPartidasComFiltroUseCase,
        ParticiparPartidaUseCase,
        CancelarParticipacaoUseCase,
        ListarPartidasFuturasUseCase,
        ExcluirPartidaUseCase,
        GerenciarParticipacaoUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ParticipacaoRepositoryPort participacaoRepositoryPort;
    private final PartidaMapper partidaMapper;
    private final ParticipacaoMapper participacaoMapper = new ParticipacaoMapper();

    public PartidaApplicationService(PartidaRepositoryPort partidaRepositoryPort,
                                     UsuarioRepositoryPort usuarioRepositoryPort,
                                     ParticipacaoRepositoryPort participacaoRepositoryPort,
                                     PartidaMapper partidaMapper) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.participacaoRepositoryPort = participacaoRepositoryPort;
        this.partidaMapper = partidaMapper;
    }

    @Override
    public PartidaResponse criarPartida(PartidaRequest request, Long criadorId) {
        validarDadosPartida(request);

        Usuario criador = buscarUsuarioPorId(criadorId);
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

    @Override
    public PartidaResponse atualizarPartida(Long id, PartidaUpdateRequest request, Long usuarioId) {
        Partida partida = buscarPartidaPorIdOuErro(id);
        partida.validarAtualizacaoPor(partida.getCriador());
        // Note: we need to re-create the usuario for permission check
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        partida.validarAtualizacaoPor(usuario);

        validarDadosAtualizacao(request);
        aplicarAtualizacoesNaPartida(partida, request);

        Partida atualizada = partidaRepositoryPort.update(partida);
        return partidaMapper.toResponseFull(atualizada);
    }

    @Override
    public List<PartidaResponse> listarPartidas() {
        return partidaRepositoryPort.findAll().stream()
                .map(partidaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PartidaResponse buscarPartidaPorId(Long id) {
        return partidaMapper.toResponseFull(buscarPartidaPorIdOuErro(id));
    }

    @Override
    public Page<PartidaResponse> buscarPartidasComFiltro(br.com.futmatch.application.dtos.requests.PartidaFiltroRequest filtro) {
        // Use existing repository methods for filtering
        Pageable pageable = getPageable(filtro);
        if (filtro.getEsporte() != null && filtro.getTipoPartida() != null) {
            return partidaRepositoryPort.findByEsporteAndTipoPartida(filtro.getEsporte(), filtro.getTipoPartida(), pageable)
                .map(partidaMapper::toResponse);
        } else if (filtro.getEsporte() != null) {
            return partidaRepositoryPort.findByEsporte(filtro.getEsporte(), pageable)
                .map(partidaMapper::toResponse);
        } else if (filtro.getTipoPartida() != null) {
            return partidaRepositoryPort.findByTipoPartida(filtro.getTipoPartida(), pageable)
                .map(partidaMapper::toResponse);
        } else if (filtro.getApenasFuturas() != null && filtro.getApenasFuturas()) {
            return partidaRepositoryPort.findAllFuturas(pageable)
                .map(partidaMapper::toResponse);
        }
        return partidaRepositoryPort.findAllFuturas(pageable)
            .map(partidaMapper::toResponse);
    }

    @Override
    public ParticipacaoResponse participarPartida(Long partidaId, Long usuarioId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        Usuario usuario = buscarUsuarioPorId(usuarioId);

        validarParticipacao(partida, usuarioId);

        Participacao participacao = new Participacao(
                usuario,
                partida,
                StatusParticipacao.PENDENTE,
                LocalDateTime.now()
        );

        Participacao participacaoSalva = participacaoRepositoryPort.save(participacao);
        return participacaoMapper.toResponse(participacaoSalva);
    }

    @Override
    public void cancelarParticipacao(Long partidaId, Long usuarioId) {
        Participacao participacao = buscarParticipacaoOuErro(partidaId, usuarioId);

        Usuario usuario = participacao.getUsuario();
        Usuario criador = participacao.getPartida().getCriador();
        if (!usuario.getId().equals(usuarioId) && !criador.getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o participante ou o criador da partida pode cancelar a participação");
        }

        participacaoRepositoryPort.delete(participacao);
    }

    @Override
    public Page<PartidaResponse> listarPartidasFuturas(Pageable pageable) {
        return partidaRepositoryPort.findAllFuturas(pageable)
                .map(partidaMapper::toResponse);
    }

    @Override
    public void excluirPartida(Long partidaId, Long criadorId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        // Excluir todas as participações da partida
        List<Participacao> participacoes = participacaoRepositoryPort.findByPartidaId(partidaId);
        participacoes.forEach(participacaoRepositoryPort::delete);

        // Excluir a partida
        partidaRepositoryPort.delete(partida);
    }

    @Override
    public ParticipacaoResponse aprovarParticipacao(Long partidaId, Long participanteId, Long criadorId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        Participacao participacao = buscarParticipacaoOuErro(partidaId, participanteId);
        validarAprovacaoParticipacao(partida, participacao);

        participacao.confirmar();
        Participacao participacaoAtualizada = participacaoRepositoryPort.save(participacao);

        return participacaoMapper.toResponse(participacaoAtualizada);
    }

    @Override
    public ParticipacaoResponse rejeitarParticipacao(Long partidaId, Long participanteId, Long criadorId) {
        Partida partida = buscarPartidaPorIdOuErro(partidaId);
        validaPermissaoDoCriador(partida, criadorId);

        Participacao participacao = buscarParticipacaoOuErro(partidaId, participanteId);
        participacao.rejeitar();
        participacaoRepositoryPort.save(participacao);

        return participacaoMapper.toResponse(participacao);
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepositoryPort.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado com ID: " + id));
    }

    private Partida buscarPartidaPorIdOuErro(Long id) {
        return partidaRepositoryPort.findById(id)
                .orElseThrow(() -> new PartidaNotFoundException("Partida não encontrada com ID: " + id));
    }

    private Participacao buscarParticipacaoOuErro(Long partidaId, Long usuarioId) {
        return participacaoRepositoryPort.findByUsuarioAndPartida(usuarioId, partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Participação não encontrada"));
    }

    private void validaPermissaoDoCriador(Partida partida, Long usuarioId) {
        if (!partida.getCriador().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode realizar esta ação");
        }
    }

    private void validarParticipacao(Partida partida, Long usuarioId) {
        // Verificar se o usuário já participa da partida
        if (participacaoRepositoryPort.existsByUsuarioAndPartida(usuarioId, partida.getId())) {
            throw new IllegalArgumentException("Usuário já participa desta partida");
        }

        // Verificar se a partida não está cheia
        long participantesConfirmados = participacaoRepositoryPort.countByPartidaAndStatus(partida.getId(), StatusParticipacao.CONFIRMADO);
        if (participantesConfirmados >= partida.getTotalJogadores()) {
            throw new IllegalArgumentException("Partida já está com o número máximo de jogadores");
        }

        // Verificar se a partida não é passada
        if (partida.isJogosJaRealizada()) {
            throw new IllegalArgumentException("Não é possível participar de uma partida que já aconteceu");
        }
    }

    private void aplicarAtualizacoesNaPartida(Partida partida, PartidaUpdateRequest request) {
        if (request.getNome() != null) partida.atualizarNome(request.getNome());
        if (request.getEsporte() != null) partida.atualizarEsporte(Esporte.valueOf(request.getEsporte().toUpperCase()));
        if (request.getLatitude() != null && request.getLongitude() != null)
            partida.atualizarLocalizacao(new Localizacao(request.getLatitude(), request.getLongitude()));
        if (request.getDataHora() != null) partida.atualizarDataHora(request.getDataHora());
        if (request.getTotalJogadores() != null) partida.atualizarTotalJogadores(request.getTotalJogadores());
        if (request.getTipoPartida() != null) partida.atualizarTipoPartida(TipoPartida.valueOf(request.getTipoPartida().toUpperCase()));
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

        if (request.getEsporte() != null && request.getTotalJogadores() != null)
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

    private void validarAprovacaoParticipacao(Partida partida, Participacao participacao) {
        if (participacao.getStatus() != StatusParticipacao.PENDENTE) {
            throw new IllegalArgumentException("Apenas participações pendentes podem ser aprovadas");
        }

        // Verificar se a partida não está cheia
        long participantesConfirmados = participacaoRepositoryPort.countByPartidaAndStatus(partida.getId(), StatusParticipacao.CONFIRMADO);
        if (participantesConfirmados >= partida.getTotalJogadores()) {
            throw new IllegalArgumentException("Partida já está com o número máximo de jogadores");
        }
    }

    private Pageable getPageable(br.com.futmatch.application.dtos.requests.PartidaFiltroRequest filtro) {
        int page = filtro.getPage() != null ? filtro.getPage() : 0;
        int size = filtro.getSize() != null ? filtro.getSize() : 10;
        return Pageable.ofSize(size).withPage(page);
    }
}
