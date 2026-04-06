package br.com.futmatch.domain.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SenhaTest {

    @Test
    void shouldCreateSenhaWithValidValue() {
        Senha senha = new Senha("Senha@123");

        assertEquals("Senha@123", senha.valor());
    }

    @Test
    void shouldHideSenhaInToString() {
        Senha senha = new Senha("Senha@123");

        assertEquals("********", senha.toString());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "short", "nounppercase1", "NOLOWERCASE1!", "NoSpecialChar1", "noupper1!"})
    void shouldRejectInvalidSenha(String valor) {
        assertThrows(IllegalArgumentException.class, () -> new Senha(valor));
    }

    @Test
    void shouldRejectWhenNoUppercase() {
        assertThrows(IllegalArgumentException.class, () ->
            new Senha("senha@123"));
    }

    @Test
    void shouldRejectWhenNoLowercase() {
        assertThrows(IllegalArgumentException.class, () ->
            new Senha("SENHA@123"));
    }

    @Test
    void shouldRejectWhenNoNumber() {
        assertThrows(IllegalArgumentException.class, () ->
            new Senha("Senha@abc"));
    }

    @Test
    void shouldRejectWhenNoSpecialChar() {
        assertThrows(IllegalArgumentException.class, () ->
            new Senha("SenhA123"));
    }
}
