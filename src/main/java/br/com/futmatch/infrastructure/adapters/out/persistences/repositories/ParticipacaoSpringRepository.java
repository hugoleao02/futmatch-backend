package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipacaoSpringRepository extends JpaRepository<ParticipacaoEntity, Long> {
    
    @Query("SELECT p FROM ParticipacaoEntity p WHERE p.usuario.id = :usuarioId AND p.partida.id = :partidaId")
    Optional<ParticipacaoEntity> findByUsuarioAndPartida(@Param("usuarioId") Long usuarioId, @Param("partidaId") Long partidaId);
    
    @Query("SELECT p FROM ParticipacaoEntity p WHERE p.partida.id = :partidaId")
    List<ParticipacaoEntity> findByPartidaId(@Param("partidaId") Long partidaId);
    
    @Query("SELECT p FROM ParticipacaoEntity p WHERE p.usuario.id = :usuarioId")
    List<ParticipacaoEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT COUNT(p) > 0 FROM ParticipacaoEntity p WHERE p.usuario.id = :usuarioId AND p.partida.id = :partidaId")
    boolean existsByUsuarioAndPartida(@Param("usuarioId") Long usuarioId, @Param("partidaId") Long partidaId);
    
    @Query("SELECT COUNT(p) FROM ParticipacaoEntity p WHERE p.partida.id = :partidaId AND p.status = :status")
    long countByPartidaAndStatus(@Param("partidaId") Long partidaId, @Param("status") StatusParticipacao status);
} 