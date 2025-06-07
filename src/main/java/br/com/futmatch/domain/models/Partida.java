package br.com.futmatch.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    
    @Builder.Default
    private List<Participacao> participantes = new ArrayList<>();
    
    public void adicionarParticipante(Participacao participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
    }
} 