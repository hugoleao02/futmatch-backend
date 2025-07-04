package br.com.futmatch.infrastructure.adapters.out.persistences.entities;

import br.com.futmatch.domain.models.enums.StatusParticipacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "participacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacaoEntity  extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private PartidaEntity partida;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusParticipacao status;
    
    @Column(name = "data_participacao", nullable = false)
    private LocalDateTime dataParticipacao;
} 