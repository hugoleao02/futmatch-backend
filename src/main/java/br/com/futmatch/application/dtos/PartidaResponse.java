package br.com.futmatch.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaResponse {
    private Long id;
    private String nome;
    private String esporte;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dataHora;
    private Integer totalJogadores;
    private String tipoPartida;
    private Long criadorId;
    private String criadorNome;
    private Integer participantesConfirmados;
} 