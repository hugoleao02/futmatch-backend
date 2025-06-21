package br.com.futmatch.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;
    
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