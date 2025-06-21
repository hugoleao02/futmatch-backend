package br.com.futmatch.domain.valueobjects;

import lombok.Value;
import java.util.regex.Pattern;

@Value
public class Senha {
    private static final int MIN_LENGTH = 8;
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    String valor;

    public Senha(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        if (valor.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Senha deve ter no mínimo " + MIN_LENGTH + " caracteres");
        }
        if (!UPPERCASE_PATTERN.matcher(valor).find()) {
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra maiúscula");
        }
        if (!LOWERCASE_PATTERN.matcher(valor).find()) {
            throw new IllegalArgumentException("Senha deve conter pelo menos uma letra minúscula");
        }
        if (!NUMBER_PATTERN.matcher(valor).find()) {
            throw new IllegalArgumentException("Senha deve conter pelo menos um número");
        }
        if (!SPECIAL_CHAR_PATTERN.matcher(valor).find()) {
            throw new IllegalArgumentException("Senha deve conter pelo menos um caractere especial");
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
} 