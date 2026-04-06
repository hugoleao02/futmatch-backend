package br.com.futmatch.application.dtos.responses;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ParticipacaoResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long partidaId;
    private String partidaNome;
    private String status;
    private LocalDateTime dataParticipacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    public Long getPartidaId() { return partidaId; }
    public void setPartidaId(Long partidaId) { this.partidaId = partidaId; }
    public String getPartidaNome() { return partidaNome; }
    public void setPartidaNome(String partidaNome) { this.partidaNome = partidaNome; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataParticipacao() { return dataParticipacao; }
    public void setDataParticipacao(LocalDateTime dataParticipacao) { this.dataParticipacao = dataParticipacao; }
}
