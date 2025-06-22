package br.com.futmatch.infrastructure.adapters.out.persistences.entities;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.TipoPartida;
import jakarta.persistence.*;
import lombok.*; // Remover @Builder daqui

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "partidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public void adicionarParticipante(ParticipacaoEntity participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
        participacao.setPartida(this);
    }
}