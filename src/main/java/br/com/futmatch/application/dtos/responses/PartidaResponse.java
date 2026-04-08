package br.com.futmatch.application.dtos.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonSerialize
public class PartidaResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String esporte;
    private Double latitude;
    private Double longitude;
    private String nomeLocal;
    private LocalDateTime dataHora;
    private Integer totalJogadores;
    private String tipoPartida;
    private Long criadorId;
    private String criadorNome;
    private Integer participantesConfirmados;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Long getCriadorId() { return criadorId; }
    public void setCriadorId(Long criadorId) { this.criadorId = criadorId; }
    public String getCriadorNome() { return criadorNome; }
    public void setCriadorNome(String criadorNome) { this.criadorNome = criadorNome; }
    public Integer getParticipantesConfirmados() { return participantesConfirmados; }
    public void setParticipantesConfirmados(Integer participantesConfirmados) { this.participantesConfirmados = participantesConfirmados; }
}
