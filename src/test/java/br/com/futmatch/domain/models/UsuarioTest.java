package br.com.futmatch.domain.models;

import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.valueobjects.Email;
import br.com.futmatch.domain.valueobjects.Senha;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void shouldCreateUsuarioWithEmailAndSenha() {
        Email email = new Email("test@example.com");
        Senha senha = new Senha("Senha@123");

        Usuario usuario = new Usuario("John Doe", email, senha);

        assertEquals("John Doe", usuario.getNome());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("Senha@123", usuario.getSenha());
        assertEquals(Set.of("ROLE_USER"), usuario.getRoles());
    }

    @Test
    void shouldAssignAdminRole() {
        Email email = new Email("admin@example.com");
        Senha senha = new Senha("Senha@123");

        Usuario usuario = new Usuario("Admin", email, senha);
        usuario.assignRole("ROLE_ADMIN");

        assertTrue(usuario.isInRole("ROLE_ADMIN"));
    }

    @Test
    void shouldContainDefaultUserRole() {
        Email email = new Email("test@example.com");
        Senha senha = new Senha("Senha@123");

        Usuario usuario = new Usuario("John", email, senha);

        assertTrue(usuario.isInRole("ROLE_USER"));
    }

    @Test
    void shouldEncodeSenhaInConstructor() {
        Email email = new Email("test@example.com");
        Senha senha = new Senha("Senha@123");
        PasswordEncoderPort fakeEncoder = new PasswordEncoderPort() {
            @Override
            public String encode(String rawPassword) { return "encoded_" + rawPassword; }
            @Override
            public boolean matches(String rawPassword, String encodedPassword) { return false; }
        };

        Usuario usuario = new Usuario("John", email, senha, fakeEncoder);

        assertEquals("encoded_Senha@123", usuario.getSenha());
    }

    @Test
    void shouldValidateEmailMatch() {
        Email email = new Email("test@example.com");
        Usuario usuario = new Usuario("John", email, new Senha("Senha@123"));

        assertTrue(usuario.hasEmail(new Email("test@example.com")));
        assertFalse(usuario.hasEmail(new Email("other@example.com")));
    }
}
