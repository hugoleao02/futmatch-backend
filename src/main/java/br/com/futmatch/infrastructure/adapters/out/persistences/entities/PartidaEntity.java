package br.com.futmatch.infrastructure.adapters.out.persistences.entities;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidas")
public class PartidaEntity extends BaseEntity{
    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Esporte esporte;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "total_jogadores", nullable = false)
    private Integer totalJogadores;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_partida", nullable = false)
    private TipoPartida tipoPartida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private UsuarioEntity criador;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ParticipacaoEntity> participantes = new ArrayList<>();

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Esporte getEsporte() { return esporte; }
    public void setEsporte(Esporte esporte) { this.esporte = esporte; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Integer getTotalJogadores() { return totalJogadores; }
    public void setTotalJogadores(Integer totalJogadores) { this.totalJogadores = totalJogadores; }
    public TipoPartida getTipoPartida() { return tipoPartida; }
    public void setTipoPartida(TipoPartida tipoPartida) { this.tipoPartida = tipoPartida; }
    public UsuarioEntity getCriador() { return criador; }
    public void setCriador(UsuarioEntity criador) { this.criador = criador; }
    public List<ParticipacaoEntity> getParticipantes() { return participantes; }
    public void setParticipantes(List<ParticipacaoEntity> participantes) { this.participantes = participantes; }

    public void adicionarParticipante(ParticipacaoEntity participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
        participacao.setPartida(this);
    }
}
