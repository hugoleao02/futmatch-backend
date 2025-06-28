package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.TipoSala;

import java.util.ArrayList;
import java.util.List;

public class Sala extends BaseModel {
    private String nome;
    private String descricao;
    private TipoSala tipoSala;
    private Usuario dono;
    private List<MembroSala> membros = new ArrayList<>();
    private List<Partida> partidas = new ArrayList<>();

    public Sala() {
        super();
    }

    public Sala(String nome, String descricao, TipoSala tipoSala, Usuario dono) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.tipoSala = tipoSala;
        this.dono = dono;
        this.initializeTimestamps();
    }

    public Sala(Long id, String nome, String descricao, TipoSala tipoSala, Usuario dono) {
        super(id, null, null);
        this.nome = nome;
        this.descricao = descricao;
        this.tipoSala = tipoSala;
        this.dono = dono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoSala getTipoSala() {
        return tipoSala;
    }

    public void setTipoSala(TipoSala tipoSala) {
        this.tipoSala = tipoSala;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public List<MembroSala> getMembros() {
        return membros;
    }

    public void setMembros(List<MembroSala> membros) {
        this.membros = membros;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }

    public void adicionarMembro(MembroSala membro) {
        if (this.membros == null) {
            this.membros = new ArrayList<>();
        }
        this.membros.add(membro);
    }

    public void adicionarPartida(Partida partida) {
        if (this.partidas == null) {
            this.partidas = new ArrayList<>();
        }
        this.partidas.add(partida);
    }

    public boolean isPublica() {
        return TipoSala.PUBLICA.equals(this.tipoSala);
    }

    public boolean isPrivada() {
        return TipoSala.PRIVADA.equals(this.tipoSala);
    }
} 