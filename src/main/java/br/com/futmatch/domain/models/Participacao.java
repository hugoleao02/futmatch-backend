package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.StatusParticipacao;

import java.time.LocalDateTime;

public class Participacao extends BaseModel {
    private Usuario usuario;
    private Partida partida;
    private StatusParticipacao status;
    private LocalDateTime dataParticipacao;

    public Participacao(Long id, Usuario usuario, Partida partida, StatusParticipacao status, LocalDateTime dataParticipacao) {
        super(id, null, null);
        this.usuario = usuario;
        this.partida = partida;
        this.status = status;
        this.dataParticipacao = dataParticipacao;
    }

    public Participacao(Usuario usuario, Partida partida, StatusParticipacao status, LocalDateTime dataParticipacao) {
        super();
        this.usuario = usuario;
        this.partida = partida;
        this.status = status;
        this.dataParticipacao = dataParticipacao;
        this.initializeTimestamps();
    }

    public Usuario getUsuario() { return usuario; }
    public Partida getPartida() { return partida; }
    public StatusParticipacao getStatus() { return status; }
    public LocalDateTime getDataParticipacao() { return dataParticipacao; }

    public void confirmar() {
        if (this.status != StatusParticipacao.PENDENTE) {
            throw new IllegalStateException("Apenas participações pendentes podem ser confirmadas");
        }
        this.status = StatusParticipacao.CONFIRMADO;
    }

    public void rejeitar() {
        if (this.status != StatusParticipacao.PENDENTE) {
            throw new IllegalStateException("Apenas participações pendentes podem ser rejeitadas");
        }
        this.status = StatusParticipacao.REJEITADO;
    }

    public void cancelar() {
        if (this.status == StatusParticipacao.PENDENTE) {
            throw new IllegalStateException("Não é possível cancelar uma participação pendente");
        }
        this.status = StatusParticipacao.CANCELADO;
    }
}
