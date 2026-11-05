package model;

import com.google.gson.annotations.Expose;

public class UsuarioModel {
    @Expose
    private String nome;

    @Expose
    private String email;

    @Expose
    private String senha;

    @Expose
    private String role;

    public UsuarioModel() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
