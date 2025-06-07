package br.com.futmatch.infrastructure.security;

import br.com.futmatch.domain.exception.UsuarioNotFoundException;
import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationUtils {

    private final TokenServicePort tokenServicePort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public Usuario getUsuarioFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new IllegalArgumentException("Token JWT não encontrado");
        }

        String email = tokenServicePort.getEmailFromToken(token);
        return usuarioRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado: " + email));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 