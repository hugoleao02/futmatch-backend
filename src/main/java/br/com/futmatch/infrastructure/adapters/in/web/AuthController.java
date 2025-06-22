package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.LoginRequest;
import br.com.futmatch.application.dtos.requests.RegisterRequest;
import br.com.futmatch.application.usecases.AutenticacaoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacaoUseCase autenticacaoUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = autenticacaoUseCase.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = autenticacaoUseCase.loginUser(request);
        return ResponseEntity.ok(response);
    }
} 