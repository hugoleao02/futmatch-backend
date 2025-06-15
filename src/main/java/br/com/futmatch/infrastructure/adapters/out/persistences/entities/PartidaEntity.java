package br.com.futmatch.infrastructure.adapters.out.persistences.entities;

import br.com.futmatch.domain.models.Esporte;
import br.com.futmatch.domain.models.TipoPartida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidas", indexes = {
    @Index(name = "idx_partida_data_hora", columnList = "data_hora"),
    @Index(name = "idx_partida_criador", columnList = "criador_id"),
    @Index(name = "idx_partida_tipo", columnList = "tipo_partida")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    @Builder.Default
    private List<ParticipacaoEntity> participantes = new ArrayList<>();
    
    public void adicionarParticipante(ParticipacaoEntity participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
        participacao.setPartida(this); // Garantir referência bidirecional
    }
} 