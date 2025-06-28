package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PartidaSpringRepository extends JpaRepository<PartidaEntity, Long> {
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.dataHora >= CURRENT_TIMESTAMP")
    Page<PartidaEntity> findAllFuturas(Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.dataHora < CURRENT_TIMESTAMP")
    Page<PartidaEntity> findAllPassadas(Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.esporte = :esporte")
    Page<PartidaEntity> findByEsporte(@Param("esporte") String esporte, Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.tipoPartida = :tipoPartida")
    Page<PartidaEntity> findByTipoPartida(@Param("tipoPartida") String tipoPartida, Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.dataHora BETWEEN :inicio AND :fim")
    Page<PartidaEntity> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);
    
    @Query("SELECT p FROM PartidaEntity p LEFT JOIN FETCH p.criador WHERE p.esporte = :esporte AND p.tipoPartida = :tipoPartida")
    Page<PartidaEntity> findByEsporteAndTipoPartida(@Param("esporte") String esporte, @Param("tipoPartida") String tipoPartida, Pageable pageable);
} 