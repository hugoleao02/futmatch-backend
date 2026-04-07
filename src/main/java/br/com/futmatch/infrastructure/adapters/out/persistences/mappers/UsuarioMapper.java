package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.HashSet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    default Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        Usuario usuario = new Usuario(entity.getId(), entity.getNome(), entity.getEmail(), entity.getSenha());
        if (entity.getRoles() != null) {
            usuario.getRoles().clear();
            usuario.getRoles().addAll(new HashSet<>(entity.getRoles()));
        }
        return usuario;
    }

    @Mapping(target = "roles", ignore = true)
    UsuarioEntity toEntity(Usuario usuario);
}
