package br.com.futmatch.domain.models;

import br.com.futmatch.domain.ports.PasswordEncoderPort;
import br.com.futmatch.domain.valueobjects.Email;
import br.com.futmatch.domain.valueobjects.Senha;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Usuario extends BaseModel {
    private String nome;
    private String email;
    private String senha;
    private String fotoPerfilUrl;
    private Set<String> roles;

    public Usuario(String nome, Email email, Senha senha) {
        super();
        this.nome = nome;
        this.email = email.valor();
        this.senha = senha.valor();
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
        this.initializeTimestamps();
    }

    public Usuario(String nome, Email email, Senha senha, PasswordEncoderPort encoder) {
        super();
        this.nome = nome;
        this.email = email.valor();
        this.senha = encoder.encode(senha.valor());
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
        this.initializeTimestamps();
    }

    /**
     * Constructor for adapter-level services that deal with raw DTO input.
     */
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

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public Set<String> getRoles() { return roles != null ? Set.copyOf(roles) : Set.of(); }

    public void assignRole(String role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public boolean isInRole(String role) {
        return this.roles != null && this.roles.contains(role);
    }

    public boolean hasEmail(Email email) {
        return this.email.equals(email.valor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
