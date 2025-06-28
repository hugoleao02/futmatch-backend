package br.com.futmatch.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Usuario extends BaseModel {
    private String nome;
    private String email;
    private String senha;
    private String fotoPerfilUrl;
    private Set<String> roles;

    public Usuario(String nome, String email, String senha) {
        super();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
        this.initializeTimestamps();
    }

    public Usuario(Long id, String nome, String email, String senha) {
        super(id, null, null);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
    }

    public Usuario() {
        super();
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
    }
}