package br.com.futmatch.domain.ports;

import java.util.Optional;

import br.com.futmatch.domain.models.Usuario;

public interface UsuarioRepositoryPort {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByEmail(String email);
} 