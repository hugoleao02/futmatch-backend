package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.PartidaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListarPartidasFuturasUseCase {
    Page<PartidaResponse> listarPartidasFuturas(Pageable pageable);
} 