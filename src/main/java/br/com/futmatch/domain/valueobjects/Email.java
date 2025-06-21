package br.com.futmatch.domain.valueobjects;

import lombok.Value;
import java.util.regex.Pattern;

@Value
public class Email {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    String valor;

    public Email(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if (!EMAIL_PATTERN.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.valor = valor.toLowerCase().trim();
    }

    @Override
    public String toString() {
        return valor;
    }
} 