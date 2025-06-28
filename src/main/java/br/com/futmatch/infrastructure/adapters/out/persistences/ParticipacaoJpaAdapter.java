package br.com.futmatch.infrastructure.adapters.out.persistences;

import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.ParticipacaoMapper;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.ParticipacaoSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParticipacaoJpaAdapter implements ParticipacaoRepositoryPort {

    private final ParticipacaoSpringRepository participacaoSpringRepository;
    private final ParticipacaoMapper participacaoMapper;

    @Override
    public Participacao save(Participacao participacao) {
        ParticipacaoEntity entity = participacaoMapper.toEntity(participacao);
        ParticipacaoEntity savedEntity = participacaoSpringRepository.save(entity);
        return participacaoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Participacao> findByUsuarioAndPartida(Long usuarioId, Long partidaId) {
        return participacaoSpringRepository.findByUsuarioAndPartida(usuarioId, partidaId)
                .map(participacaoMapper::toDomain);
    }

    @Override
    public List<Participacao> findByPartidaId(Long partidaId) {
        return participacaoSpringRepository.findByPartidaId(partidaId).stream()
                .map(participacaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Participacao> findByUsuarioId(Long usuarioId) {
        return participacaoSpringRepository.findByUsuarioId(usuarioId).stream()
                .map(participacaoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Participacao participacao) {
        ParticipacaoEntity entity = participacaoMapper.toEntity(participacao);
        participacaoSpringRepository.delete(entity);
    }

    @Override
    public boolean existsByUsuarioAndPartida(Long usuarioId, Long partidaId) {
        return participacaoSpringRepository.existsByUsuarioAndPartida(usuarioId, partidaId);
    }

    @Override
    public long countByPartidaAndStatus(Long partidaId, StatusParticipacao status) {
        return participacaoSpringRepository.countByPartidaAndStatus(partidaId, status);
    }
} 