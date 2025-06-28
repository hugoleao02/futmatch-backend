package br.com.futmatch.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaFiltroRequest {
    private String esporte;
    private String tipoPartida;
    private Double latitude;
    private Double longitude;
    private Double raioKm;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Boolean apenasFuturas;
    private Integer page;
    private Integer size;
} 