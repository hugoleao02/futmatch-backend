package br.com.futmatch.infrastructure.adapters.out.persistences.mappers;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.infrastructure.adapters.out.persistences.entities.ParticipacaoEntity;

public class ParticipacaoMapper {

    public ParticipacaoEntity toEntity(Participacao participacao) {
        if (participacao == null) return null;
        ParticipacaoEntity entity = new ParticipacaoEntity();
        entity.setId(participacao.getId());
        entity.setStatus(participacao.getStatus());
        entity.setDataParticipacao(participacao.getDataParticipacao());
        return entity;
    }

    public Participacao toDomain(ParticipacaoEntity entity) {
        if (entity == null) return null;
        return new Participacao(
                entity.getId(),
                null, // usuario loaded separately
                null, // partida loaded separately
                entity.getStatus(),
                entity.getDataParticipacao()
        );
    }

    public ParticipacaoResponse toResponse(Participacao participacao) {
        if (participacao == null) return null;
        ParticipacaoResponse response = new ParticipacaoResponse();
        response.setId(participacao.getId());
        response.setStatus(participacao.getStatus() != null ? participacao.getStatus().name() : null);
        response.setDataParticipacao(participacao.getDataParticipacao());
        if (participacao.getUsuario() != null) {
            response.setUsuarioId(participacao.getUsuario().getId());
            response.setUsuarioNome(participacao.getUsuario().getNome());
        }
        if (participacao.getPartida() != null) {
            response.setPartidaId(participacao.getPartida().getId());
            response.setPartidaNome(participacao.getPartida().getNome());
        }
        return response;
    }
}
