package br.com.futmatch.infrastructure.health;

import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final PartidaRepositoryPort partidaRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    @Override
    public Health health() {
        try {
            // Verifica acesso ao repositório de partidas
            partidaRepository.findAllFuturas(org.springframework.data.domain.PageRequest.of(0, 1));
            
            // Verifica acesso ao repositório de usuários
            usuarioRepository.findByEmail("test@example.com");
            
            return Health.up()
                .withDetail("partidas", "OK")
                .withDetail("usuarios", "OK")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withException(e)
                .build();
        }
    }
} 