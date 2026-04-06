package br.com.futmatch.infrastructure.adapters.out.persistences.entities;

import br.com.futmatch.domain.models.enums.StatusParticipacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participacoes")
public class ParticipacaoEntity extends BaseEntity {

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

    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }
    public PartidaEntity getPartida() { return partida; }
    public void setPartida(PartidaEntity partida) { this.partida = partida; }
    public StatusParticipacao getStatus() { return status; }
    public void setStatus(StatusParticipacao status) { this.status = status; }
    public LocalDateTime getDataParticipacao() { return dataParticipacao; }
    public void setDataParticipacao(LocalDateTime dataParticipacao) { this.dataParticipacao = dataParticipacao; }
}
