package br.com.futmatch.domain.valueobjects;

import br.com.futmatch.domain.exception.SenhaFracaException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class Senha {
    private static final Pattern SENHA_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    );
    
    private final String value;
    
    public Senha(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new SenhaFracaException("Senha não pode ser nula ou vazia");
        }
        
        if (!SENHA_PATTERN.matcher(value).matches()) {
            throw new SenhaFracaException(
                "A senha deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, " +
                "minúsculas, números e caracteres especiais"
            );
        }
        
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
} 