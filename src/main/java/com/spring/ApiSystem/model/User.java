package com.spring.ApiSystem.model;

import jakarta.persistence.*;

@Entity(name = "usuario")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String senha;

    private String nome;

    private boolean ativo = true;

    public User(Long id, String email, String cpf, String senha, String nome, boolean ativo, String role) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.nome = nome;
        this.ativo = ativo;
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
