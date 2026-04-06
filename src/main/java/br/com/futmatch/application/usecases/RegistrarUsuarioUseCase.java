package br.com.futmatch.application.usecases;

import br.com.futmatch.application.dtos.responses.AuthResponse;
import br.com.futmatch.application.dtos.requests.RegisterRequest;

public interface RegistrarUsuarioUseCase {
    AuthResponse registrarUsuario(RegisterRequest request);
}
