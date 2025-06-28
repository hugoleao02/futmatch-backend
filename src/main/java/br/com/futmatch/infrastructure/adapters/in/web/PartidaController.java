package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.requests.PartidaRequest;
import br.com.futmatch.application.dtos.responses.PartidaResponse;
import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.application.dtos.requests.PartidaUpdateRequest;
import br.com.futmatch.application.usecases.*;
import br.com.futmatch.domain.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private final CriarPartidaUseCase criarPartidaUseCase;
    private final AtualizarPartidaUseCase atualizarPartidaUseCase;
    private final ListarPartidasUseCase listarPartidasUseCase;
    private final BuscarPartidaPorIdUseCase buscarPartidaPorIdUseCase;
    private final ParticiparPartidaUseCase participarPartidaUseCase;
    private final CancelarParticipacaoUseCase cancelarParticipacaoUseCase;
    private final ListarPartidasFuturasUseCase listarPartidasFuturasUseCase;
    private final ExcluirPartidaUseCase excluirPartidaUseCase;

    @PostMapping
    public ResponseEntity<PartidaResponse> criarPartida(
            @Valid @RequestBody PartidaRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        PartidaResponse response = criarPartidaUseCase.criarPartida(request, usuario.getId());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponse> atualizarPartida(
            @PathVariable Long id,
            @Valid @RequestBody PartidaUpdateRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        PartidaResponse response = atualizarPartidaUseCase.atualizarPartida(id, request, usuario.getId());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PartidaResponse>> listarPartidas() {
        List<PartidaResponse> partidas = listarPartidasUseCase.listarPartidas();
        return ResponseEntity.ok(partidas);
    }

    @GetMapping("/futuras")
    public ResponseEntity<Page<PartidaResponse>> listarPartidasFuturas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PartidaResponse> partidas = listarPartidasFuturasUseCase.listarPartidasFuturas(pageable);
        return ResponseEntity.ok(partidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponse> buscarPartidaPorId(@PathVariable Long id) {
        PartidaResponse partida = buscarPartidaPorIdUseCase.buscarPartidaPorId(id);
        return ResponseEntity.ok(partida);
    }

    @PostMapping("/{id}/participar")
    public ResponseEntity<ParticipacaoResponse> participarPartida(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        ParticipacaoResponse response = participarPartidaUseCase.participarPartida(id, usuario.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/cancelar-participacao")
    public ResponseEntity<Void> cancelarParticipacao(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        cancelarParticipacaoUseCase.cancelarParticipacao(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPartida(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        excluirPartidaUseCase.excluirPartida(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teste-auth")
    public ResponseEntity<String> testeAutenticacao(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }
        return ResponseEntity.ok("Usuário autenticado: " + usuario.getNome() + " (ID: " + usuario.getId() + ")");
    }
} 