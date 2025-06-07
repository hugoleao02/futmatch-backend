package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.AuthResponse;
import br.com.futmatch.application.dtos.LoginRequest;
import br.com.futmatch.application.dtos.RegisterRequest;
import br.com.futmatch.application.usecases.AutenticacaoUseCase;
import br.com.futmatch.domain.exception.EmailAlreadyExistsException;
import br.com.futmatch.domain.exception.InvalidCredentialsException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoApplicationService implements AutenticacaoUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenServicePort tokenServicePort;

    // Métodos públicos que lidam com DTOs (entrada da aplicação)
    public AuthResponse registerUser(RegisterRequest request) {
        Usuario usuario = registerUser(request.getNome(), request.getEmail(), request.getSenha());
        String token = tokenServicePort.generateToken(usuario.getEmail());

        return AuthResponse.builder()
            .token(token)
            .id(usuario.getId())
            .email(usuario.getEmail())
            .nome(usuario.getNome())
            .build();
    }

    public AuthResponse loginUser(LoginRequest request) {
        Usuario usuario = loginUser(request.getEmail(), request.getSenha());
        String token = tokenServicePort.generateToken(usuario.getEmail());

        return AuthResponse.builder()
            .token(token)
            .id(usuario.getId())
            .email(usuario.getEmail())
            .nome(usuario.getNome())
            .build();
    }

    // Implementação dos Use Cases (lógica de negócio pura)
    @Override
    public Usuario registerUser(String nome, String email, String senha) {
        if (usuarioRepositoryPort.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + email);
        }

        Usuario usuario = new Usuario(
            nome,
            email,
            passwordEncoderPort.encode(senha)
        );

        return usuarioRepositoryPort.save(usuario);
    }

    @Override
    public Usuario loginUser(String email, String senha) {
        Usuario usuario = usuarioRepositoryPort.findByEmail(email)
            .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!passwordEncoderPort.matches(senha, usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        return usuario;
    }
} 