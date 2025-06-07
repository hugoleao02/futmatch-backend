package br.com.futmatch.application.services;

import br.com.futmatch.application.dtos.AuthResponse;
import br.com.futmatch.application.dtos.RegisterRequest;
import br.com.futmatch.application.usecases.AutenticacaoUseCase;
import br.com.futmatch.domain.exception.EmailAlreadyExistsException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import br.com.futmatch.infrastructure.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoApplicationService implements AutenticacaoUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse registerUser(RegisterRequest request) {
        if (usuarioRepositoryPort.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado: " + request.getEmail());
        }

        Usuario usuario = new Usuario(
            request.getNome(),
            request.getEmail(),
            passwordEncoder.encode(request.getSenha())
        );

        Usuario savedUsuario = usuarioRepositoryPort.save(usuario);
        String token = jwtUtils.generateToken(savedUsuario.getEmail());

        return AuthResponse.builder()
            .token(token)
            .id(savedUsuario.getId())
            .email(savedUsuario.getEmail())
            .nome(savedUsuario.getNome())
            .build();
    }
} 