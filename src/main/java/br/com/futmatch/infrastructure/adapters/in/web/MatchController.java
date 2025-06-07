package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.PartidaRequest;
import br.com.futmatch.application.dtos.PartidaResponse;
import br.com.futmatch.application.usecases.CriarPartidaUseCase;
import br.com.futmatch.domain.models.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final CriarPartidaUseCase criarPartidaUseCase;

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
} 