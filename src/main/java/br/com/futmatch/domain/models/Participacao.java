package br.com.futmatch.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participacao {
    private Long id;
    private Usuario usuario;
    private Partida partida;
    private StatusParticipacao status;
    private LocalDateTime dataParticipacao;
} 