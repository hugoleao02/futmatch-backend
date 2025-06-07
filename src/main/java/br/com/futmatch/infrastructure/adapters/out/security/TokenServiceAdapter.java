package br.com.futmatch.infrastructure.adapters.out.security;

import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.infrastructure.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenServiceAdapter implements TokenServicePort {
    
    private final JwtUtils jwtUtils;
    
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