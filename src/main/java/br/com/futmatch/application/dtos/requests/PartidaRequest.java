package br.com.futmatch.application.dtos.requests;

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
public class PartidaRequest {
    
    @NotBlank(message = "Nome da partida é obrigatório")
    private String nome;
    
    @NotBlank(message = "Esporte é obrigatório")
    private String esporte;
    
    @NotNull(message = "Latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    private Double latitude;
    
    @NotNull(message = "Longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    private Double longitude;
    
    @NotNull(message = "Data e hora são obrigatórias")
    @FutureOrPresent(message = "Data e hora devem ser no presente ou futuro")
    private LocalDateTime dataHora;
    
    @NotNull(message = "Total de jogadores é obrigatório")
    @Min(value = 2, message = "Mínimo de 2 jogadores")
    @Max(value = 22, message = "Máximo de 22 jogadores")
    private Integer totalJogadores;
    
    @NotBlank(message = "Tipo da partida é obrigatório")
    private String tipoPartida;
} 