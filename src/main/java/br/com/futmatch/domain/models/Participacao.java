package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.StatusParticipacao;
import java.time.LocalDateTime;


public class Participacao extends BaseModel {
    private Usuario usuario;
    private Partida partida;
    private StatusParticipacao status;
    private LocalDateTime dataParticipacao;

    public Participacao() {
        super();
    }

    public Participacao(Long id, Usuario usuario, Partida partida, StatusParticipacao status, LocalDateTime dataParticipacao) {
        super(id, null, null);
        this.usuario = usuario;
        this.partida = partida;
        this.status = status;
        this.dataParticipacao = dataParticipacao;
    }

    public Participacao(Usuario criador, Partida partida, StatusParticipacao statusParticipacao, LocalDateTime now) {
        super();
        this.usuario = criador;
        this.partida = partida;
        this.status = statusParticipacao;
        this.dataParticipacao = now;
        this.initializeTimestamps();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public StatusParticipacao getStatus() {
        return status;
    }

    public void setStatus(StatusParticipacao status) {
        this.status = status;
    }

    public LocalDateTime getDataParticipacao() {
        return dataParticipacao;
    }

    public void setDataParticipacao(LocalDateTime dataParticipacao) {
        this.dataParticipacao = dataParticipacao;
    }
}