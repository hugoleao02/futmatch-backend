package br.com.futmatch.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${app.jwt.key-rotation-interval:86400000}") // 24 horas em milissegundos
    private long keyRotationInterval;

    private final Map<String, SecretKey> keyCache = new ConcurrentHashMap<>();
    private long lastKeyRotation = System.currentTimeMillis();

    private Key getSigningKey() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastKeyRotation > keyRotationInterval) {
            keyCache.clear();
            lastKeyRotation = currentTime;
        }

        String keyId = String.valueOf(currentTime / keyRotationInterval);
        return keyCache.computeIfAbsent(keyId, k -> {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        });
    }

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("keyId", String.valueOf(System.currentTimeMillis() / keyRotationInterval));
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            return claims.getSubject();
        } catch (JwtException e) {
            throw new SecurityException("Token inválido ou expirado", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            // Verifica se o token não está expirado
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 