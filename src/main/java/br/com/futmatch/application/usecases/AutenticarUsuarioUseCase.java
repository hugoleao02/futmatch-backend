package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.LoginRequest;

public interface AutenticarUsuarioUseCase {
    AuthResponse autenticarUsuario(LoginRequest request);
}
