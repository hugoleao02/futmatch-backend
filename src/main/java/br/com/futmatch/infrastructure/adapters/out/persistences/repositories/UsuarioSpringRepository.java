package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;

import java.util.Optional;

@Repository
public interface UsuarioSpringRepository extends JpaRepository<UsuarioEntity, Long> {
    @Query("SELECT u FROM UsuarioEntity u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<UsuarioEntity> findByEmail(String email);
} 