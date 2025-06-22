package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UsuarioMapper.class})
public interface ParticipacaoMapper {
    
    ParticipacaoEntity toEntity(Participacao participacao);
    
    @Mapping(target = "partida", ignore = true)
    Participacao toDomain(ParticipacaoEntity entity);
} 