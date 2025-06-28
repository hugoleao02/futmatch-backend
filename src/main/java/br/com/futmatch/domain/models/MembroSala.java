package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.StatusMembroSala;
import br.com.futmatch.domain.models.enums.TipoMembroSala;

import java.time.LocalDateTime;

public class MembroSala extends BaseModel {
    private Usuario usuario;
    private Sala sala;
    private TipoMembroSala tipoMembro;
    private StatusMembroSala status;
    private LocalDateTime dataEntrada;

    public MembroSala() {
        super();
    }

    public MembroSala(Usuario usuario, Sala sala, TipoMembroSala tipoMembro, StatusMembroSala status) {
        super();
        this.usuario = usuario;
        this.sala = sala;
        this.tipoMembro = tipoMembro;
        this.status = status;
        this.dataEntrada = LocalDateTime.now();
        this.initializeTimestamps();
    }

    public MembroSala(Long id, Usuario usuario, Sala sala, TipoMembroSala tipoMembro, StatusMembroSala status, LocalDateTime dataEntrada) {
        super(id, null, null);
        this.usuario = usuario;
        this.sala = sala;
        this.tipoMembro = tipoMembro;
        this.status = status;
        this.dataEntrada = dataEntrada;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public TipoMembroSala getTipoMembro() {
        return tipoMembro;
    }

    public void setTipoMembro(TipoMembroSala tipoMembro) {
        this.tipoMembro = tipoMembro;
    }

    public StatusMembroSala getStatus() {
        return status;
    }

    public void setStatus(StatusMembroSala status) {
        this.status = status;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public boolean isDono() {
        return TipoMembroSala.DONO.equals(this.tipoMembro);
    }

    public boolean isAdministrador() {
        return TipoMembroSala.ADMINISTRADOR.equals(this.tipoMembro);
    }

    public boolean isMembro() {
        return TipoMembroSala.MEMBRO.equals(this.tipoMembro);
    }

    public boolean isAprovado() {
        return StatusMembroSala.APROVADO.equals(this.status);
    }

    public boolean isPendente() {
        return StatusMembroSala.PENDENTE.equals(this.status);
    }
} 