package com.spring.ApiSystem.domain.entity;

public class User {

    private Long id;
    private String email;
    private String cpf;
    private String senha;
    private String nome;

    private boolean ativo = true;

    public User(boolean ativo, String cpf, String email, Long id, String nome, String senha) {
        this.ativo = ativo;
        this.cpf = cpf;
        this.email = email;
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
