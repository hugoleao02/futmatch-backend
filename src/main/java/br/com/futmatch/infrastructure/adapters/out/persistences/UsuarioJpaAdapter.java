package br.com.futmatch.infrastructure.adapters.out.persistences;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.UsuarioSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {

    private final UsuarioSpringRepository usuarioSpringRepository;

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioSpringRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioSpringRepository.findByEmail(email);
    }
} 