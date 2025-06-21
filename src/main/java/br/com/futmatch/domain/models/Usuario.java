package br.com.futmatch.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements Serializable {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String fotoPerfilUrl;
    private Set<String> roles;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
    }
} 