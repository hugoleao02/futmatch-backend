package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartidaMapper {

    @Mapping(target = "esporte", source = "esporte", qualifiedByName = "esporteEnumToString")
    @Mapping(target = "tipoPartida", source = "tipoPartida", qualifiedByName = "tipoPartidaEnumToString")
    @Mapping(target = "participantesConfirmados", expression = "java(calcularParticipantesConfirmados(partida.getParticipantes()))")
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    PartidaResponse toResponse(Partida partida);

    default PartidaResponse toResponseFull(Partida partida) {
        if (partida == null) return null;
        PartidaResponse response = toResponse(partida);
        if (partida.getLocalizacao() != null) {
            response.setLatitude(partida.getLocalizacao().latitude());
            response.setLongitude(partida.getLocalizacao().longitude());
        }
        if (partida.getCriador() != null) {
            response.setCriadorId(partida.getCriador().getId());
            response.setCriadorNome(partida.getCriador().getNome());
        }
        response.setNomeLocal(partida.getNomeLocal());
        return response;
    }

    @Named("esporteEnumToString")
    default String esporteEnumToString(Esporte esporte) {
        return esporte != null ? esporte.name() : null;
    }

    @Named("tipoPartidaEnumToString")
    default String tipoPartidaEnumToString(TipoPartida tipoPartida) {
        return tipoPartida != null ? tipoPartida.name() : null;
    }

    @Named("calcularParticipantesConfirmados")
    default Integer calcularParticipantesConfirmados(List<Participacao> participantes) {
        if (participantes == null) return 0;
        return (int) participantes.stream()
                .filter(p -> p.getStatus() == StatusParticipacao.CONFIRMADO)
                .count();
    }

    default PartidaEntity toEntity(Partida partida) {
        if (partida == null) return null;
        PartidaEntity entity = new PartidaEntity();
        entity.setId(partida.getId());
        entity.setNome(partida.getNome());
        entity.setEsporte(partida.getEsporte());
        if (partida.getLocalizacao() != null) {
            entity.setLatitude(partida.getLocalizacao().latitude());
            entity.setLongitude(partida.getLocalizacao().longitude());
        }
        entity.setDataHora(partida.getDataHora());
        entity.setTotalJogadores(partida.getTotalJogadores());
        entity.setTipoPartida(partida.getTipoPartida());
        entity.setNomeLocal(partida.getNomeLocal());
        return entity;
    }

    default Partida toDomain(PartidaEntity entity) {
        if (entity == null) return null;
        br.com.futmatch.domain.valueobjects.Localizacao localizacao = null;
        if (entity.getLatitude() != null && entity.getLongitude() != null) {
            localizacao = br.com.futmatch.domain.valueobjects.Localizacao.criadaPara(entity.getLatitude(), entity.getLongitude());
        }
        Usuario criador = null;
        if (entity.getCriador() != null) {
            UsuarioEntity uc = entity.getCriador();
            criador = new Usuario(uc.getId(), uc.getNome(), uc.getEmail(), uc.getSenha());
        }
        return new Partida(
                entity.getId(),
                entity.getNome(),
                entity.getEsporte(),
                localizacao,
                entity.getDataHora(),
                entity.getTotalJogadores(),
                entity.getTipoPartida(),
                criador,
                entity.getNomeLocal()
        );
    }
}
