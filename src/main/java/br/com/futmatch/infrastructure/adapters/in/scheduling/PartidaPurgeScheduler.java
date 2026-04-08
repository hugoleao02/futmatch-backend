package br.com.futmatch.infrastructure.adapters.in.scheduling;

import br.com.futmatch.application.usecases.PurgarPartidasEncerradasUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.partidas.purge", name = "enabled", havingValue = "true", matchIfMissing = true)
public class PartidaPurgeScheduler {

    private static final Logger log = LoggerFactory.getLogger(PartidaPurgeScheduler.class);

    private final PurgarPartidasEncerradasUseCase purgarPartidasEncerradasUseCase;

    public PartidaPurgeScheduler(PurgarPartidasEncerradasUseCase purgarPartidasEncerradasUseCase) {
        this.purgarPartidasEncerradasUseCase = purgarPartidasEncerradasUseCase;
    }

    @Scheduled(cron = "${app.partidas.purge.cron:0 0 3 * * *}")
    public void executarLimpeza() {
        int n = purgarPartidasEncerradasUseCase.executar();
        if (n > 0) {
            log.info("Limpeza automática: {} partida(s) removida(s).", n);
        }
    }
}
