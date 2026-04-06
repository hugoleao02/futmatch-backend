package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.LoginRequest;
import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.domain.exception.InvalidCredentialsException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenServicePort tokenServicePort;

    @InjectMocks
    private AutenticarUsuarioUseCaseImpl autenticarUsuarioUseCase;

    @Test
    @DisplayName("Should authenticate user successfully and return AuthResponse")
    void autenticarUsuario_shouldReturnAuthResponse_whenCredentialsAreValid() {
        LoginRequest request = buildLoginRequest("user@example.com", "password123");
        Usuario usuario = new Usuario(1L, "User", "user@example.com", "$2a$encodedPassword");

        when(usuarioRepositoryPort.findByEmail("user@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("password123", "$2a$encodedPassword")).thenReturn(true);
        when(tokenServicePort.generateToken("user@example.com")).thenReturn("jwt-token-abc");

        AuthResponse response = autenticarUsuarioUseCase.autenticarUsuario(request);

        assertNotNull(response);
        assertEquals("jwt-token-abc", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("user@example.com", response.getEmail());
        assertEquals("User", response.getNome());

        verify(usuarioRepositoryPort).findByEmail("user@example.com");
        verify(passwordEncoderPort).matches("password123", "$2a$encodedPassword");
        verify(tokenServicePort).generateToken("user@example.com");
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when email is not found")
    void autenticarUsuario_shouldThrowInvalidCredentialsException_whenEmailNotFound() {
        LoginRequest request = buildLoginRequest("unknown@example.com", "password123");

        when(usuarioRepositoryPort.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> autenticarUsuarioUseCase.autenticarUsuario(request)
        );

        assertEquals("Credenciais invalidas", exception.getMessage());
        verify(usuarioRepositoryPort).findByEmail("unknown@example.com");
        verifyNoInteractions(passwordEncoderPort);
        verifyNoInteractions(tokenServicePort);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password does not match")
    void autenticarUsuario_shouldThrowInvalidCredentialsException_whenPasswordDoesNotMatch() {
        LoginRequest request = buildLoginRequest("user@example.com", "wrongpassword");
        Usuario usuario = new Usuario(1L, "User", "user@example.com", "$2a$encodedPassword");

        when(usuarioRepositoryPort.findByEmail("user@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("wrongpassword", "$2a$encodedPassword")).thenReturn(false);

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> autenticarUsuarioUseCase.autenticarUsuario(request)
        );

        assertEquals("Credenciais invalidas", exception.getMessage());
        verify(usuarioRepositoryPort).findByEmail("user@example.com");
        verify(passwordEncoderPort).matches("wrongpassword", "$2a$encodedPassword");
        verifyNoInteractions(tokenServicePort);
    }

    @Test
    @DisplayName("Should generate token only when authentication succeeds")
    void autenticarUsuario_shouldGenerateToken_whenAuthenticationSucceeds() {
        LoginRequest request = buildLoginRequest("user@example.com", "correctpass");
        Usuario usuario = new Usuario(5L, "ValidUser", "user@example.com", "$2a$hash");

        when(usuarioRepositoryPort.findByEmail("user@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("correctpass", "$2a$hash")).thenReturn(true);
        when(tokenServicePort.generateToken("user@example.com")).thenReturn("token-xyz");

        autenticarUsuarioUseCase.autenticarUsuario(request);

        verify(tokenServicePort).generateToken("user@example.com");
    }

    @Test
    @DisplayName("Should return correct user details in AuthResponse")
    void autenticarUsuario_shouldReturnCorrectUserDetails() {
        LoginRequest request = buildLoginRequest("maria@example.com", "mariaSenha");
        Usuario usuario = new Usuario(10L, "Maria Silva", "maria@example.com", "$2a$mhashed");

        when(usuarioRepositoryPort.findByEmail("maria@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoderPort.matches("mariaSenha", "$2a$mhashed")).thenReturn(true);
        when(tokenServicePort.generateToken("maria@example.com")).thenReturn("maria-token");

        AuthResponse response = autenticarUsuarioUseCase.autenticarUsuario(request);

        assertEquals("maria-token", response.getToken());
        assertEquals(10L, response.getId());
        assertEquals("maria@example.com", response.getEmail());
        assertEquals("Maria Silva", response.getNome());
    }

    private LoginRequest buildLoginRequest(String email, String senha) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setSenha(senha);
        return request;
    }
}
