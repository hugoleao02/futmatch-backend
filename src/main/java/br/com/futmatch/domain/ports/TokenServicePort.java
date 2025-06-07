package br.com.futmatch.domain.ports;

public interface TokenServicePort {
    String generateToken(String email);
    String getEmailFromToken(String token);
    boolean validateToken(String token);
} 