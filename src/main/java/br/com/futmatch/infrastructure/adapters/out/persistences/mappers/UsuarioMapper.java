package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {
    
    @Mapping(target = "roles", expression = "java(usuario.getRoles() != null ? new java.util.HashSet<>(usuario.getRoles()) : new java.util.HashSet<>())")
    UsuarioEntity toEntity(Usuario usuario);
    
    @Mapping(target = "roles", expression = "java(entity.getRoles() != null ? new java.util.HashSet<>(entity.getRoles()) : new java.util.HashSet<>())")
    Usuario toDomain(UsuarioEntity entity);
} 