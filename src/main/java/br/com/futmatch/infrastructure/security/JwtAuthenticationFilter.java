package br.com.futmatch.infrastructure.security;

import br.com.futmatch.domain.models.Usuario;
import br.com.futmatch.domain.ports.TokenServicePort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenServicePort tokenServicePort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Processando requisição para: {}", request.getRequestURI());
        log.debug("Método HTTP: {}", request.getMethod());

        // Verifica se é um endpoint que não precisa de autenticação
        if (isPublicEndpoint(request)) {
            log.debug("Endpoint público detectado: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractTokenFromRequest(request);
        log.debug("Token extraído: {}", token != null ? token.substring(0, Math.min(30, token.length())) + "..." : "null");

        if (token != null && tokenServicePort.validateToken(token)) {
            try {
                String email = tokenServicePort.getEmailFromToken(token);
                log.debug("Email do token: {}", email);
                
                Optional<Usuario> usuarioOpt = usuarioRepositoryPort.findByEmail(email);

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();
                    log.debug("Usuário encontrado: {}", usuario.getNome());
                    log.debug("Roles do usuário: {}", usuario.getRoles());
                    
                    List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    log.debug("Authorities criadas: {}", authorities);

                    UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Autenticação definida com sucesso para usuário: {}", usuario.getNome());
                } else {
                    log.warn("Usuário não encontrado para email: {}", email);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Usuário não encontrado ou não autorizado");
                    return;
                }
            } catch (Exception e) {
                log.error("Erro ao processar token: {}", e.getMessage(), e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido ou erro de autenticação");
                return;
            }
        } else {
            log.warn("Token inválido ou nulo para endpoint protegido: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido ou ausente");
            return;
        }

        log.debug("Continuando com o filtro para: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/auth/") ||
               requestURI.startsWith("/swagger-ui/") ||
               requestURI.equals("/swagger-ui.html") ||
               requestURI.startsWith("/v3/api-docs") ||
               requestURI.startsWith("/swagger-resources") ||
               requestURI.startsWith("/webjars/") ||
               requestURI.startsWith("/actuator/health") ||
               requestURI.equals("/");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            // Remove aspas duplas extras se existirem
            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }
            return token;
        }
        return null;
    }
} 