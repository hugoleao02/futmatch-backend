package br.com.futmatch.application.dtos.requests;

import jakarta.validation.constraints.NotNull;

public class ParticipacaoRequest {

    @NotNull(message = "ID da partida é obrigatório")
    private Long partidaId;

    public Long getPartidaId() { return partidaId; }
    public void setPartidaId(Long partidaId) { this.partidaId = partidaId; }
}
