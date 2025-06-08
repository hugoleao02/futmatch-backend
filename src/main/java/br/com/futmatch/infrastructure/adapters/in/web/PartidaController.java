package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.PartidaRequest;
import br.com.futmatch.application.dtos.PartidaResponse;
import br.com.futmatch.application.dtos.PartidaUpdateRequest;
import br.com.futmatch.application.usecases.AtualizarPartidaUseCase;
import br.com.futmatch.application.usecases.CriarPartidaUseCase;
import br.com.futmatch.application.usecases.ListarPartidasUseCase;
import br.com.futmatch.domain.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
} 