package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partida {
    private Long id;
    private String nome;
    private Esporte esporte;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dataHora;
    private Integer totalJogadores;
    private TipoPartida tipoPartida;
    private Usuario criador;

    private List<Participacao> participantes = new ArrayList<>();

    public void adicionarParticipante(Participacao participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
    }

    public Partida() {
    }

    public Partida(Long id, String nome, Esporte esporte, Double latitude, Double longitude, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador) {
        this.id = id;
        this.nome = nome;
        this.esporte = esporte;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.totalJogadores = totalJogadores;
        this.tipoPartida = tipoPartida;
        this.criador = criador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Esporte getEsporte() {
        return esporte;
    }

    public void setEsporte(Esporte esporte) {
        this.esporte = esporte;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getTotalJogadores() {
        return totalJogadores;
    }

    public void setTotalJogadores(Integer totalJogadores) {
        this.totalJogadores = totalJogadores;
    }

    public TipoPartida getTipoPartida() {
        return tipoPartida;
    }

    public void setTipoPartida(TipoPartida tipoPartida) {
        this.tipoPartida = tipoPartida;
    }

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public List<Participacao> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participacao> participantes) {
        this.participantes = participantes;
    }
}