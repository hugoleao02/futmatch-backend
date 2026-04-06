package br.com.futmatch.domain.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmailWithValidFormat() {
        Email email = new Email("test@example.com");

        assertEquals("test@example.com", email.valor());
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        Email email = new Email("Test@Example.COM");

        assertEquals("test@example.com", email.valor());
    }

    @Test
    void shouldTrimWhitespace() {
        Email email = new Email("  test@example.com  ");

        assertEquals("test@example.com", email.valor());
    }

    @Test
    void shouldEqualByEmailValue() {
        Email email1 = new Email("test@example.com");
        Email email2 = new Email("test@example.com");

        assertEquals(email1, email2);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  ", "invalid", "@domain.com", "user@"})
    void shouldRejectInvalidEmail(String valor) {
        assertThrows(IllegalArgumentException.class, () -> new Email(valor));
    }
}
