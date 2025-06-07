package br.com.futmatch.infrastructure.adapters.out.persistences;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.mappers.UsuarioMapper;
import br.com.futmatch.infrastructure.adapters.out.persistences.repositories.UsuarioSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {

    private final UsuarioSpringRepository usuarioSpringRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioSpringRepository.save(entity);
        return usuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioSpringRepository.findByEmail(email)
                .map(usuarioMapper::toDomain);
    }
} 