package br.com.futmatch.infrastructure.security;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenServicePort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromRequest(request);
        System.out.println("JWT Filter - Token extraído: " + (token != null ? token.substring(0, Math.min(30, token.length())) + "..." : "null"));

        if (token != null && tokenServicePort.validateToken(token)) {
            try {
                String email = tokenServicePort.getEmailFromToken(token);
                System.out.println("JWT Filter - Email do token: " + email);
                
                Optional<Usuario> usuarioOpt = usuarioRepositoryPort.findByEmail(email);

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    System.out.println("JWT Filter - Usuário encontrado: " + usuario.getNome());
                    
                    List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JWT Filter - Autenticação definida com sucesso");
                } else {
                    System.out.println("JWT Filter - Usuário não encontrado para email: " + email);
                }
            } catch (Exception e) {
                // Token inválido - continua sem autenticação
                System.out.println("JWT Filter - Erro ao processar token: " + e.getMessage());
                logger.debug("Token JWT inválido: " + e.getMessage());
            }
        } else {
            System.out.println("JWT Filter - Token inválido ou nulo");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 