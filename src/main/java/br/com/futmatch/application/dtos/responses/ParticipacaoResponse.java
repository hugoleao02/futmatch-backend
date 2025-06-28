package br.com.futmatch.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long partidaId;
    private String partidaNome;
    private String status;
    private LocalDateTime dataParticipacao;
} 