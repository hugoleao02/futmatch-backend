package br.com.futmatch.application.usecases.impl;

import br.com.futmatch.application.usecases.PurgarPartidasEncerradasUseCase;
import br.com.futmatch.domain.models.Participacao;
import br.com.futmatch.domain.models.Partida;
import br.com.futmatch.domain.ports.ParticipacaoRepositoryPort;
import br.com.futmatch.domain.ports.PartidaRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurgarPartidasEncerradasUseCaseImpl implements PurgarPartidasEncerradasUseCase {

    private final PartidaRepositoryPort partidaRepositoryPort;
    private final ParticipacaoRepositoryPort participacaoRepositoryPort;
    private final int diasAposPartida;

    public PurgarPartidasEncerradasUseCaseImpl(
            PartidaRepositoryPort partidaRepositoryPort,
            ParticipacaoRepositoryPort participacaoRepositoryPort,
            @Value("${app.partidas.purge.dias-apos-partida:1}") int diasAposPartida) {
        this.partidaRepositoryPort = partidaRepositoryPort;
        this.participacaoRepositoryPort = participacaoRepositoryPort;
        this.diasAposPartida = Math.max(0, diasAposPartida);
    }

    @Override
    @Transactional
    public int executar() {
        LocalDateTime limite = LocalDateTime.now().minusDays(diasAposPartida);
        List<Partida> candidatas = partidaRepositoryPort.findByDataHoraBefore(limite);
        int removidas = 0;
        for (Partida partida : candidatas) {
            List<Participacao> participacoes = participacaoRepositoryPort.findByPartidaId(partida.getId());
            participacoes.forEach(participacaoRepositoryPort::delete);
            partidaRepositoryPort.delete(partida);
            removidas++;
        }
        return removidas;
    }
}
