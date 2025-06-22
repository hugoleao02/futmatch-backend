package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import java.util.List;

public interface ListarPartidasUseCase {
    List<PartidaResponse> listarPartidas();
} 