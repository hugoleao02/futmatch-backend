package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;

import java.util.Optional;

@Repository
public interface UsuarioSpringRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);
} 