package br.com.futmatch.infrastructure.health;

import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import br.com.futmatch.domain.ports.UsuarioRepositoryPort;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final PartidaRepositoryPort partidaRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public CustomHealthIndicator(PartidaRepositoryPort partidaRepository,
                                 UsuarioRepositoryPort usuarioRepository) {
        this.partidaRepository = partidaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Health health() {
        try {
            partidaRepository.findAllFuturas(PageRequest.of(0, 1));
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
