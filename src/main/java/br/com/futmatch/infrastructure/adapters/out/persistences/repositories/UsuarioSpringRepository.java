package br.com.futmatch.infrastructure.adapters.out.persistences.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.futmatch.domain.models.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioSpringRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
} 