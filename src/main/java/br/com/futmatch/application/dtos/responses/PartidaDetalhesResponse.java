package br.com.futmatch.application.dtos.responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonSerialize
public class PartidaDetalhesResponse implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private List<ParticipanteInfo> participantes;
    private boolean isCriador;
    private boolean isParticipando;
    private boolean hasSolicitado;
    private List<ParticipanteInfo> solicitacoes;
    private Object times;

    public PartidaDetalhesResponse() {}

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
    public List<ParticipanteInfo> getParticipantes() { return participantes; }
    public void setParticipantes(List<ParticipanteInfo> participantes) { this.participantes = participantes; }
    public boolean isCriador() { return isCriador; }
    public void setIsCriador(boolean isCriador) { this.isCriador = isCriador; }
    public boolean isParticipando() { return isParticipando; }
    public void setIsParticipando(boolean isParticipando) { this.isParticipando = isParticipando; }
    public boolean isHasSolicitado() { return hasSolicitado; }
    public void setHasSolicitado(boolean hasSolicitado) { this.hasSolicitado = hasSolicitado; }
    public List<ParticipanteInfo> getSolicitacoes() { return solicitacoes; }
    public void setSolicitacoes(List<ParticipanteInfo> solicitacoes) { this.solicitacoes = solicitacoes; }
    public Object getTimes() { return times; }
    public void setTimes(Object times) { this.times = times; }

    public static class ParticipanteInfo implements Serializable {
        private Long id;
        private String nome;
        private String fotoPerfilUrl;
        private String status;

        public ParticipanteInfo() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getFotoPerfilUrl() { return fotoPerfilUrl; }
        public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
