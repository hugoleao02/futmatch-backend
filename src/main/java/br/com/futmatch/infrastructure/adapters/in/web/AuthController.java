package br.com.futmatch.infrastructure.adapters.in.web;

import br.com.futmatch.application.dtos.AuthResponse;
import br.com.futmatch.application.dtos.LoginRequest;
import br.com.futmatch.application.dtos.RegisterRequest;
import br.com.futmatch.application.services.AutenticacaoApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacaoApplicationService autenticacaoApplicationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = autenticacaoApplicationService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = autenticacaoApplicationService.loginUser(request);
        return ResponseEntity.ok(response);
    }
} 