package br.com.futmatch.application.usecases;

import br.com.futmatch.domain.models.Usuario;

public interface AutenticacaoUseCase {
    Usuario registerUser(String nome, String email, String senha);
    Usuario loginUser(String email, String senha);
} 