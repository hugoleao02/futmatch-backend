package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.StatusParticipacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
public class Participacao {
    private Long id;
    private Usuario usuario;
    private Partida partida;
    private StatusParticipacao status;
    private LocalDateTime dataParticipacao;

    public Participacao() {
    }

    public Participacao(Long id, Usuario usuario, Partida partida, StatusParticipacao status, LocalDateTime dataParticipacao) {
        this.id = id;
        this.usuario = usuario;
        this.partida = partida;
        this.status = status;
        this.dataParticipacao = dataParticipacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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