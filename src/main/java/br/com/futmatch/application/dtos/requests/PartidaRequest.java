package br.com.futmatch.application.dtos.requests;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

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

    @Size(max = 512, message = "Nome do local deve ter no máximo 512 caracteres")
    private String nomeLocal;

    @NotNull(message = "Data e hora são obrigatórias")
    @FutureOrPresent(message = "Data e hora devem ser no presente ou futuro")
    private LocalDateTime dataHora;

    @NotNull(message = "Total de jogadores é obrigatório")
    @Min(value = 2, message = "Mínimo de 2 jogadores")
    @Max(value = 22, message = "Máximo de 22 jogadores")
    private Integer totalJogadores;

    @NotBlank(message = "Tipo da partida é obrigatório")
    private String tipoPartida;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEsporte() { return esporte; }
    public void setEsporte(String esporte) { this.esporte = esporte; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getNomeLocal() { return nomeLocal; }
    public void setNomeLocal(String nomeLocal) { this.nomeLocal = nomeLocal; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Integer getTotalJogadores() { return totalJogadores; }
    public void setTotalJogadores(Integer totalJogadores) { this.totalJogadores = totalJogadores; }
    public String getTipoPartida() { return tipoPartida; }
    public void setTipoPartida(String tipoPartida) { this.tipoPartida = tipoPartida; }
}
