package br.com.futmatch.domain.models;

import br.com.futmatch.domain.models.enums.Esporte;
import br.com.futmatch.domain.models.enums.StatusParticipacao;
import br.com.futmatch.domain.models.enums.TipoPartida;
import br.com.futmatch.domain.valueobjects.Localizacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partida extends BaseModel {
    private String nome;
    private Esporte esporte;
    private Localizacao localizacao;
    /** Nome ou endereço legível do local (ex.: resultado do autocomplete). */
    private String nomeLocal;
    private LocalDateTime dataHora;
    private Integer totalJogadores;
    private TipoPartida tipoPartida;
    private Usuario criador;

    private List<Participacao> participantes = new ArrayList<>();

    private Partida() {
        super();
    }

    public Partida(String nome, Esporte esporte, Localizacao localizacao, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador) {
        this(nome, esporte, localizacao, dataHora, totalJogadores, tipoPartida, criador, null);
    }

    public Partida(String nome, Esporte esporte, Localizacao localizacao, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador, String nomeLocal) {
        super();
        this.nome = nome;
        this.esporte = esporte;
        this.localizacao = localizacao;
        this.nomeLocal = nomeLocal;
        this.dataHora = dataHora;
        this.totalJogadores = totalJogadores;
        this.tipoPartida = tipoPartida;
        this.criador = criador;

        adicionarParticipante(new Participacao(criador, this, StatusParticipacao.CONFIRMADO, LocalDateTime.now()));
        this.initializeTimestamps();
    }

    public Partida(Long id, String nome, Esporte esporte, Localizacao localizacao, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador) {
        this(id, nome, esporte, localizacao, dataHora, totalJogadores, tipoPartida, criador, null);
    }

    public Partida(Long id, String nome, Esporte esporte, Localizacao localizacao, LocalDateTime dataHora,
                   Integer totalJogadores, TipoPartida tipoPartida, Usuario criador, String nomeLocal) {
        super(id, null, null);
        this.nome = nome;
        this.esporte = esporte;
        this.localizacao = localizacao;
        this.nomeLocal = nomeLocal;
        this.dataHora = dataHora;
        this.totalJogadores = totalJogadores;
        this.tipoPartida = tipoPartida;
        this.criador = criador;
    }

    public String getNome() { return nome; }
    public Esporte getEsporte() { return esporte; }
    public Localizacao getLocalizacao() { return localizacao; }
    public String getNomeLocal() { return nomeLocal; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Integer getTotalJogadores() { return totalJogadores; }
    public TipoPartida getTipoPartida() { return tipoPartida; }
    public Usuario getCriador() { return criador; }
    public List<Participacao> getParticipantes() { return participantes; }

    public void adicionarParticipante(Participacao participacao) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        this.participantes.add(participacao);
    }

    public boolean estaCheia() {
        return totalParticipantes() >= totalJogadores;
    }

    public int totalParticipantes() {
        return participantes != null ? participantes.size() : 0;
    }

    public int totalConfirmados() {
        if (participantes == null) return 0;
        return (int) participantes.stream()
                .filter(p -> p.getStatus() == StatusParticipacao.CONFIRMADO)
                .count();
    }

    public boolean temParticipacaoDo(Usuario usuario) {
        if (participantes == null) return false;
        return participantes.stream()
                .anyMatch(p -> p.getUsuario().getEmail().equals(usuario.getEmail()));
    }

    public boolean ehCriadoPor(Usuario usuario) {
        return criador != null && criador.getEmail().equals(usuario.getEmail());
    }

    public boolean ehFutura() {
        return dataHora != null && dataHora.isAfter(LocalDateTime.now());
    }

    public boolean isJogosJaRealizada() {
        return dataHora != null && dataHora.isBefore(LocalDateTime.now());
    }

    public void atualizarNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
    }

    public void atualizarLocalizacao(Localizacao localizacao) {
        if (localizacao != null) {
            this.localizacao = localizacao;
        }
    }

    public void atualizarNomeLocal(String nomeLocal) {
        if (nomeLocal != null && !nomeLocal.isBlank()) {
            this.nomeLocal = nomeLocal.trim();
        }
    }

    public void atualizarDataHora(LocalDateTime dataHora) {
        if (dataHora != null) {
            this.dataHora = dataHora;
        }
    }

    public void atualizarTotalJogadores(Integer totalJogadores) {
        if (totalJogadores != null && totalJogadores > 0) {
            this.totalJogadores = totalJogadores;
        }
    }

    public void atualizarEsporte(Esporte esporte) {
        if (esporte != null) {
            this.esporte = esporte;
        }
    }

    public void atualizarTipoPartida(TipoPartida tipoPartida) {
        if (tipoPartida != null) {
            this.tipoPartida = tipoPartida;
        }
    }

    public boolean podeParticipar() {
        return !isJogosJaRealizada() && !estaCheia();
    }

    public void validarAtualizacaoPor(Usuario usuario) {
        if (!ehCriadoPor(usuario)) {
            throw new IllegalArgumentException("Apenas o criador da partida pode atualizá-la");
        }
    }
}
