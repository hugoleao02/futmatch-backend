package br.com.futmatch.application.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaUpdateRequest {
    
    private String nome;
    
    private String esporte;
    
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;
    
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;
    
    @FutureOrPresent(message = "Data e hora devem ser no presente ou futuro")
    private LocalDateTime dataHora;
    
    @Min(value = 2, message = "Mínimo de 2 jogadores")
    @Max(value = 22, message = "Máximo de 22 jogadores")
    private Integer totalJogadores;
    
    private String tipoPartida;
} 