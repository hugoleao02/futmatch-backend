package br.com.futmatch.infrastructure.adapters.out.security;

import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.infrastructure.security.JwtUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenServiceAdapter implements TokenServicePort {

    private final JwtUtils jwtUtils;

    public TokenServiceAdapter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String generateToken(String email) {
        return jwtUtils.generateToken(email);
    }

    @Override
    public String getEmailFromToken(String token) {
        return jwtUtils.getEmailFromToken(token);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }
}
