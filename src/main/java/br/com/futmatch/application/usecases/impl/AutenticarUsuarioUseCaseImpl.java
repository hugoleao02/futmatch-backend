package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.LoginRequest;
import br.com.futmatch.application.usecases.AutenticarUsuarioUseCase;
import br.com.futmatch.domain.exception.InvalidCredentialsException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class AutenticarUsuarioUseCaseImpl implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenServicePort tokenServicePort;

    public AutenticarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepositoryPort,
                                        PasswordEncoderPort passwordEncoderPort,
                                        TokenServicePort tokenServicePort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    public AuthResponse autenticarUsuario(LoginRequest request) {
        Usuario usuario = usuarioRepositoryPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciais invalidas"));

        if (!passwordEncoderPort.matches(request.getSenha(), usuario.getSenha())) {
            throw new InvalidCredentialsException("Credenciais invalidas");
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
