package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.responses.ParticipacaoResponse;
import br.com.futmatch.application.usecases.ParticiparPartidaUseCase;
import br.com.futmatch.application.usecases.CancelarParticipacaoUseCase;
import br.com.futmatch.application.usecases.GerenciarParticipacaoUseCase;
import br.com.futmatch.domain.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participacoes")
@RequiredArgsConstructor
public class ParticipacaoController {

    private final ParticiparPartidaUseCase participarPartidaUseCase;
    private final CancelarParticipacaoUseCase cancelarParticipacaoUseCase;
    private final GerenciarParticipacaoUseCase gerenciarParticipacaoUseCase;

    @PostMapping("/partida/{partidaId}")
    public ResponseEntity<ParticipacaoResponse> participarPartida(
            @PathVariable Long partidaId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        ParticipacaoResponse response = participarPartidaUseCase.participarPartida(partidaId, usuario.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/partida/{partidaId}")
    public ResponseEntity<Void> cancelarParticipacao(
            @PathVariable Long partidaId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        cancelarParticipacaoUseCase.cancelarParticipacao(partidaId, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/partida/{partidaId}/participante/{participanteId}/aprovar")
    public ResponseEntity<ParticipacaoResponse> aprovarParticipacao(
            @PathVariable Long partidaId,
            @PathVariable Long participanteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        ParticipacaoResponse response = gerenciarParticipacaoUseCase.aprovarParticipacao(partidaId, participanteId, usuario.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/partida/{partidaId}/participante/{participanteId}/rejeitar")
    public ResponseEntity<ParticipacaoResponse> rejeitarParticipacao(
            @PathVariable Long partidaId,
            @PathVariable Long participanteId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não autenticado");
        }
        
        ParticipacaoResponse response = gerenciarParticipacaoUseCase.rejeitarParticipacao(partidaId, participanteId, usuario.getId());
        return ResponseEntity.ok(response);
    }
} 