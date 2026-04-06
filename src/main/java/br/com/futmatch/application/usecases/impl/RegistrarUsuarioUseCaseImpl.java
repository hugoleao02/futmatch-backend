package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.RegisterRequest;
import br.com.futmatch.application.usecases.RegistrarUsuarioUseCase;
import br.com.futmatch.domain.exception.EmailAlreadyExistsException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenServicePort tokenServicePort;

    public RegistrarUsuarioUseCaseImpl(UsuarioRepositoryPort usuarioRepositoryPort,
                                       PasswordEncoderPort passwordEncoderPort,
                                       TokenServicePort tokenServicePort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenServicePort = tokenServicePort;
    }

    @Override
    public AuthResponse registrarUsuario(RegisterRequest request) {
        if (usuarioRepositoryPort.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email ja cadastrado: " + request.getEmail());
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
}
