package br.com.futmatch.domain.ports;

import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.enums.StatusParticipacao;

import java.util.List;
import java.util.Optional;

public interface ParticipacaoRepositoryPort {
    Participacao save(Participacao participacao);
    Optional<Participacao> findByUsuarioAndPartida(Long usuarioId, Long partidaId);
    List<Participacao> findByPartidaId(Long partidaId);
    List<Participacao> findByUsuarioId(Long usuarioId);
    void delete(Participacao participacao);
    boolean existsByUsuarioAndPartida(Long usuarioId, Long partidaId);
    long countByPartidaAndStatus(Long partidaId, StatusParticipacao status);
} 