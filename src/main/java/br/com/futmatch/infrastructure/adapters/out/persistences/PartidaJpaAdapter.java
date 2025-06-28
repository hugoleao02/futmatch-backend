package br.com.futmatch.infrastructure.adapters.out.persistences;

import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.PartidaMapper;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.PartidaSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PartidaJpaAdapter implements PartidaRepositoryPort {

    private final PartidaSpringRepository partidaSpringRepository;
    private final PartidaMapper partidaMapper;

    @Override
    public Partida save(Partida partida) {
        PartidaEntity entity = partidaMapper.toEntity(partida);
        PartidaEntity savedEntity = partidaSpringRepository.save(entity);
        return partidaMapper.toDomain(savedEntity);
    }

    @Override
    @Cacheable(value = "partidas", key = "#id")
    public Optional<Partida> findById(Long id) {
        return partidaSpringRepository.findById(id)
                .map(partidaMapper::toDomain);
    }

    @Override
    @CacheEvict(value = "partidas", allEntries = true)
    public Partida update(Partida partida) {
        PartidaEntity entity = partidaMapper.toEntity(partida);
        PartidaEntity updatedEntity = partidaSpringRepository.save(entity);
        return partidaMapper.toDomain(updatedEntity);
    }

    @Override
    public List<Partida> findAll() {
        return partidaSpringRepository.findAll().stream()
                .map(partidaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "partidas", key = "'futuras_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Partida> findAllFuturas(Pageable pageable) {
        return partidaSpringRepository.findAllFuturas(pageable)
                .map(partidaMapper::toDomain);
    }

    @Override
    @Cacheable(value = "partidas", key = "'passadas_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Partida> findAllPassadas(Pageable pageable) {
        return partidaSpringRepository.findAllPassadas(pageable)
                .map(partidaMapper::toDomain);
    }

    @Override
    @CacheEvict(value = "partidas", allEntries = true)
    public void delete(Partida partida) {
        PartidaEntity entity = partidaMapper.toEntity(partida);
        partidaSpringRepository.delete(entity);
    }

    @Override
    public Page<Partida> findByEsporte(String esporte, Pageable pageable) {
        return partidaSpringRepository.findByEsporte(esporte, pageable)
            .map(partidaMapper::toDomain);
    }

    @Override
    public Page<Partida> findByTipoPartida(String tipoPartida, Pageable pageable) {
        return partidaSpringRepository.findByTipoPartida(tipoPartida, pageable)
            .map(partidaMapper::toDomain);
    }

    @Override
    public Page<Partida> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        return partidaSpringRepository.findByDataHoraBetween(inicio, fim, pageable)
            .map(partidaMapper::toDomain);
    }

    @Override
    public Page<Partida> findByEsporteAndTipoPartida(String esporte, String tipoPartida, Pageable pageable) {
        return partidaSpringRepository.findByEsporteAndTipoPartida(esporte, tipoPartida, pageable)
            .map(partidaMapper::toDomain);
    }
} 