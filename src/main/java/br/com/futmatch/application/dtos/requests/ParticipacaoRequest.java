package br.com.futmatch.application.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoRequest {
    
    @NotNull(message = "ID da partida é obrigatório")
    private Long partidaId;
}
