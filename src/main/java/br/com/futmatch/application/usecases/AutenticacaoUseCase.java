package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.LoginRequest;
import br.com.futmatch.application.dtos.requests.RegisterRequest;

public interface AutenticacaoUseCase {
    AuthResponse registerUser(RegisterRequest request);
    AuthResponse loginUser(LoginRequest request);
} 