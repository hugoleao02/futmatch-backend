package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UsuarioMapper.class, ParticipacaoMapper.class})
public interface PartidaMapper {
    
    @Mapping(target = "participantes", ignore = true)
    PartidaEntity toEntity(Partida partida);
    
    Partida toDomain(PartidaEntity entity);
    
    default PartidaEntity toEntityWithParticipantes(Partida partida) {
        PartidaEntity entity = toEntity(partida);
        
        if (partida.getParticipantes() != null) {
            for (Participacao participacao : partida.getParticipantes()) {
                ParticipacaoEntity participacaoEntity = ParticipacaoEntity.builder()
                        .usuario(participacao.getUsuario() != null ? 
                            UsuarioEntity.builder().id(participacao.getUsuario().getId()).build() : null)
                        .status(participacao.getStatus())
                        .dataParticipacao(participacao.getDataParticipacao())
                        .build();
                entity.adicionarParticipante(participacaoEntity);
            }
        }
        
        return entity;
    }
} 