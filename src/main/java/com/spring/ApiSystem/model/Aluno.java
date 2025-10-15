package com.spring.ApiSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Aluno{
    @ManyToOne
    private Usuario usuario;

    @Column(unique = true, nullable = false)
    private String cpf;

    public Aluno() {
    }

    public Aluno(Usuario usuario, String cpf) {
        this.usuario = usuario;
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
