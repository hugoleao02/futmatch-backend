package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaSpringRepository extends JpaRepository<PartidaEntity, Long> {
} 