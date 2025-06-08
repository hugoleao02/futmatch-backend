package br.com.futmatch.domain.exception;

public class PartidaNotFoundException extends RuntimeException {
    public PartidaNotFoundException(String message) {
        super(message);
    }
} 