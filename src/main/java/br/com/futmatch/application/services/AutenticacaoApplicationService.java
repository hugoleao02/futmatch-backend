package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.LoginRequest;
import br.com.futmatch.application.dtos.requests.RegisterRequest;
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

    @Override
    public AuthResponse registerUser(RegisterRequest request) {
        if (usuarioRepositoryPort.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + request.getEmail());
        }

        Usuario usuario = new Usuario(
            request.getNome(),
            request.getEmail(),
            passwordEncoderPort.encode(request.getSenha())
        );

        usuario = usuarioRepositoryPort.save(usuario);
        String token = tokenServicePort.generateToken(usuario.getEmail());

        return AuthResponse.builder()
            .token(token)
            .id(usuario.getId())
            .email(usuario.getEmail())
            .nome(usuario.getNome())
            .build();
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        Usuario usuario = usuarioRepositoryPort.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!passwordEncoderPort.matches(request.getSenha(), usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = tokenServicePort.generateToken(usuario.getEmail());

        return AuthResponse.builder()
            .token(token)
            .id(usuario.getId())
            .email(usuario.getEmail())
            .nome(usuario.getNome())
            .build();
    }
} 