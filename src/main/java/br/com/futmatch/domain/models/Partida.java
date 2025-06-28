package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Partida extends BaseModel {
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
        super();
    }

    public Partida(Long id, String nome, Esporte esporte, Double latitude, Double longitude, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador) {
        super(id, null, null);
        this.nome = nome;
        this.esporte = esporte;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.totalJogadores = totalJogadores;
        this.tipoPartida = tipoPartida;
        this.criador = criador;
    }

    public Partida(String nome, Esporte esporte, Double latitude, Double longitude, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador) {
        super();
        this.nome = nome;
        this.esporte = esporte;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
        this.totalJogadores = totalJogadores;
        this.tipoPartida = tipoPartida;
        this.criador = criador;
        this.initializeTimestamps();
    }
}