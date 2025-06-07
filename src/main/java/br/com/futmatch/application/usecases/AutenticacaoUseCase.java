package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.AuthResponse;
import br.com.futmatch.application.dtos.RegisterRequest;

public interface AutenticacaoUseCase {
    AuthResponse registerUser(RegisterRequest request);
} 