package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UsuarioMapper.class})
public interface ParticipacaoMapper {
    
    @Mapping(target = "partida", ignore = true)
    ParticipacaoEntity toEntity(Participacao participacao);
    
    @Mapping(target = "partida", ignore = true)
    Participacao toDomain(ParticipacaoEntity entity);
    
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nome", target = "usuarioNome")
    @Mapping(source = "partida.id", target = "partidaId")
    @Mapping(source = "partida.nome", target = "partidaNome")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusEnumToString")
    ParticipacaoResponse toResponse(Participacao participacao);
    
    @Named("statusEnumToString")
    default String statusEnumToString(br.com.futmatch.domain.models.enums.StatusParticipacao status) {
        return status != null ? status.name() : null;
    }
} 