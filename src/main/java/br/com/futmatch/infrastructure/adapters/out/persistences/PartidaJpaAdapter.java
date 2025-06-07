package br.com.futmatch.infrastructure.adapters.out.persistences;

import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.PartidaSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartidaJpaAdapter implements PartidaRepositoryPort {

    private final PartidaSpringRepository partidaSpringRepository;
    private final PartidaMapper partidaMapper;

    @Override
    public Partida save(Partida partida) {
        PartidaEntity entity = partidaMapper.toEntityWithParticipantes(partida);
        PartidaEntity savedEntity = partidaSpringRepository.save(entity);
        return partidaMapper.toDomain(savedEntity);
    }
} 