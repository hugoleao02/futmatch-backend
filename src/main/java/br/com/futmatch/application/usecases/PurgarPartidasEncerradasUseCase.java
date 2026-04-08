package br.com.futmatch.application.usecases;

/**
 * Remove partidas cuja data/hora já passou há mais de N dias (limpeza automática).
 */
public interface PurgarPartidasEncerradasUseCase {

    /**
     * @return quantidade de partidas excluídas
     */
    int executar();
}
