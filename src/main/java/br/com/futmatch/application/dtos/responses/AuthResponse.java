package br.com.futmatch.application.dtos.responses;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String token;
    private Long id;
    private String email;
    private String nome;

    private AuthResponse() {}

    private AuthResponse(String token, Long id, String email, String nome) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nome = nome;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private Long id;
        private String email;
        private String nome;

        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder id(Long id) { this.id = id; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder nome(String nome) { this.nome = nome; return this; }
        public AuthResponse build() { return new AuthResponse(token, id, email, nome); }
    }

    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
}
