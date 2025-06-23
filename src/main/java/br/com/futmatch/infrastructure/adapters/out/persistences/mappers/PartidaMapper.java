package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.PartidaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UsuarioMapper.class, ParticipacaoMapper.class})
@Component
public interface PartidaMapper {

    @Mapping(target = "participantes", ignore = true)
    PartidaEntity toEntity(Partida partida);

    Partida toDomain(PartidaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criador", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    @Mapping(source = "esporte", target = "esporte", qualifiedByName = "stringToEsporte")
    @Mapping(source = "tipoPartida", target = "tipoPartida", qualifiedByName = "stringToTipoPartida")
    Partida toDomain(PartidaRequest request);

    @Mapping(source = "criador.id", target = "criadorId")
    @Mapping(source = "criador.nome", target = "criadorNome")
    @Mapping(source = "esporte", target = "esporte", qualifiedByName = "esporteEnumToString")
    @Mapping(source = "tipoPartida", target = "tipoPartida", qualifiedByName = "tipoPartidaEnumToString")
    @Mapping(target = "participantesConfirmados", expression = "java(calcularParticipantesConfirmados(partida.getParticipantes()))")
    PartidaResponse toResponse(Partida partida);

    // --- Métodos de Conversão Customizados (String para Enum) ---
    // Usados para mapear String do DTO de Request para Enum do Modelo de Domínio.
    // Anotados com @Named para serem referenciados nos @Mapping do MapStruct.
    // Incluem validação de nulo/vazio e tratamento de exceção para valores inválidos.

    @Named("stringToEsporte")
    default Esporte stringToEsporte(String esporteString) {
        if (esporteString == null || esporteString.trim().isEmpty()) {
            throw new IllegalArgumentException("Esporte não pode ser nulo ou vazio.");
        }
        try {
            return Esporte.valueOf(esporteString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de Esporte inválido: " + esporteString);
        }
    }

    @Named("stringToTipoPartida")
    default TipoPartida stringToTipoPartida(String tipoPartidaString) {
        if (tipoPartidaString == null || tipoPartidaString.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de Partida não pode ser nulo ou vazio.");
        }
        try {
            return TipoPartida.valueOf(tipoPartidaString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Valor de TipoPartida inválido: '" + tipoPartidaString + "'. Erro: " + e.getMessage());
            throw new IllegalArgumentException("Valor de Tipo de Partida inválido: " + tipoPartidaString);
        }
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
        if (participantes == null) {
            return 0;
        }
        return (int) participantes.stream()
                .filter(p -> p.getStatus() == StatusParticipacao.CONFIRMADO)
                .count();
    }

}