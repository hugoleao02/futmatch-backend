package br.com.futmatch.domain.models.enums;

public enum Esporte {
    FUTEBOL(22),
    FUTSAL(10),
    SOCIETY(10);

    private final int totalMaximoJogadores;

    Esporte(int totalMaximoJogadores) {
        this.totalMaximoJogadores = totalMaximoJogadores;
    }

    public int totalMaximoJogadores() {
        return totalMaximoJogadores;
    }
} 