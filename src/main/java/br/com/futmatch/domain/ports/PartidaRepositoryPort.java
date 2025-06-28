package br.com.futmatch.domain.ports;

import br.com.futmatch.domain.models.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartidaRepositoryPort {
    Partida save(Partida partida);
    Optional<Partida> findById(Long id);
    Partida update(Partida partida);
    List<Partida> findAll();
    Page<Partida> findAllFuturas(Pageable pageable);
    Page<Partida> findAllPassadas(Pageable pageable);
    void delete(Partida partida);
    Page<Partida> findByEsporte(String esporte, Pageable pageable);
    Page<Partida> findByTipoPartida(String tipoPartida, Pageable pageable);
    Page<Partida> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
    Page<Partida> findByEsporteAndTipoPartida(String esporte, String tipoPartida, Pageable pageable);
} 