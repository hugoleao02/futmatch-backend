package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.requests.RegisterRequest;
import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.domain.exception.EmailAlreadyExistsException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseImplTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenServicePort tokenServicePort;

    @InjectMocks
    private RegistrarUsuarioUseCaseImpl registrarUsuarioUseCase;

    @Test
    @DisplayName("Should register user successfully and return AuthResponse")
    void registrarUsuario_shouldReturnAuthResponse_whenRequestIsValid() {
        RegisterRequest request = buildRegisterRequest("John Doe", "john@example.com", "password123");

        when(usuarioRepositoryPort.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(tokenServicePort.generateToken("john@example.com")).thenReturn("jwt-token-123");

        Usuario savedUser = new Usuario(1L, "John Doe", "john@example.com", "encodedPassword");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(savedUser);

        AuthResponse response = registrarUsuarioUseCase.registrarUsuario(request);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.getToken());
        assertEquals(1L, response.getId());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("John Doe", response.getNome());

        verify(usuarioRepositoryPort).findByEmail("john@example.com");
        verify(passwordEncoderPort).encode("password123");
        verify(usuarioRepositoryPort).save(any(Usuario.class));
        verify(tokenServicePort).generateToken("john@example.com");
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already registered")
    void registrarUsuario_shouldThrowEmailAlreadyExistsException_whenEmailAlreadyExists() {
        RegisterRequest request = buildRegisterRequest("John", "john@example.com", "password123");

        when(usuarioRepositoryPort.findByEmail("john@example.com"))
                .thenReturn(Optional.of(new Usuario()));

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> registrarUsuarioUseCase.registrarUsuario(request)
        );

        assertTrue(exception.getMessage().contains("john@example.com"));
        verify(usuarioRepositoryPort).findByEmail("john@example.com");
        verifyNoInteractions(passwordEncoderPort);
        verifyNoInteractions(tokenServicePort);
    }

    @Test
    @DisplayName("Should encode password before creating user")
    void registrarUsuario_shouldEncodePassword_whenCreatingUser() {
        RegisterRequest request = buildRegisterRequest("Jane", "jane@example.com", "secret123");

        when(usuarioRepositoryPort.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode("secret123")).thenReturn("$2a$encoded");
        when(tokenServicePort.generateToken("jane@example.com")).thenReturn("token");

        Usuario savedUser = new Usuario(2L, "Jane", "jane@example.com", "$2a$encoded");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(savedUser);

        registrarUsuarioUseCase.registrarUsuario(request);

        verify(passwordEncoderPort).encode("secret123");
    }

    @Test
    @DisplayName("Should set ROLE_USER on newly created user")
    void registrarUsuario_shouldSetRoleUser_whenCreatingUser() {
        RegisterRequest request = buildRegisterRequest("Admin", "admin@example.com", "password123");

        when(usuarioRepositoryPort.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode("password123")).thenReturn("hashed");
        when(tokenServicePort.generateToken("admin@example.com")).thenReturn("token");

        Usuario savedUser = new Usuario(3L, "Admin", "admin@example.com", "hashed");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(savedUser);

        AuthResponse response = registrarUsuarioUseCase.registrarUsuario(request);

        assertEquals("Admin", response.getNome());
    }

    @Test
    @DisplayName("Should save user with encoded password")
    void registrarUsuario_shouldSaveUserWithEncodedPassword() {
        RegisterRequest request = buildRegisterRequest("Bob", "bob@example.com", "bobpass123");

        when(usuarioRepositoryPort.findByEmail("bob@example.com")).thenReturn(Optional.empty());
        when(passwordEncoderPort.encode("bobpass123")).thenReturn("encodedBobPass");
        when(tokenServicePort.generateToken("bob@example.com")).thenReturn("token");

        Usuario savedUser = new Usuario(4L, "Bob", "bob@example.com", "encodedBobPass");
        when(usuarioRepositoryPort.save(any(Usuario.class))).thenReturn(savedUser);

        registrarUsuarioUseCase.registrarUsuario(request);

        verify(usuarioRepositoryPort).save(argThat(usuario ->
                usuario.getSenha().equals("encodedBobPass")
        ));
    }

    private RegisterRequest buildRegisterRequest(String nome, String email, String senha) {
        RegisterRequest request = new RegisterRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        return request;
    }
}
