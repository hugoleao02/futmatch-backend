package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaSpringRepository extends JpaRepository<PartidaEntity, Long> {
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.dataHora >= CURRENT_TIMESTAMP")
    Page<PartidaEntity> findAllFuturas(Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.dataHora < CURRENT_TIMESTAMP")
    Page<PartidaEntity> findAllPassadas(Pageable pageable);
} 