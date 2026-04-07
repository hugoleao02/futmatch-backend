package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.PartidaDetalhesResponse;

public interface BuscarPartidaDetalhesUseCase {
    PartidaDetalhesResponse buscarDetalhes(Long partidaId, Long usuarioId);
}
