package br.com.futmatch.infrastructure.security;

import br.com.futmatch.infrastructure.adapters.in.web.JsonApiErrorWriter;
import br.com.futmatch.infrastructure.adapters.in.web.dto.ErrorCode;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JsonApiErrorWriter jsonApiErrorWriter;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, JsonApiErrorWriter jsonApiErrorWriter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jsonApiErrorWriter = jsonApiErrorWriter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/**",
                    "/error",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/actuator/health/**"
                ).permitAll()
                .requestMatchers("/api/partidas/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/participacoes/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((request, response, authException) -> {
                    try {
                        jsonApiErrorWriter.write(response, HttpStatus.UNAUTHORIZED, ErrorCode.ACESSO_NAO_AUTORIZADO,
                                "Acesso não autorizado");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    try {
                        jsonApiErrorWriter.write(response, HttpStatus.FORBIDDEN, ErrorCode.ACESSO_NEGADO,
                                "Acesso negado");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
            )
            .addFilterBefore(rateLimitingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:[*]", "http://127.0.0.1:[*]"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OncePerRequestFilter rateLimitingFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain filterChain) throws ServletException, IOException {

                if (request.getRequestURI().startsWith("/actuator/health") ||
                    request.getRequestURI().startsWith("/api/auth/")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String ip = request.getRemoteAddr();
                Bucket bucket = buckets.computeIfAbsent(ip, k -> createNewBucket());

                if (bucket.tryConsume(1)) {
                    filterChain.doFilter(request, response);
                } else {
                    try {
                        jsonApiErrorWriter.write(response, HttpStatus.TOO_MANY_REQUESTS, ErrorCode.RATE_LIMIT_EXCEDIDO,
                                "Limite de requisições excedido. Tente novamente em instantes.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.simple(100, Duration.ofMinutes(1));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
