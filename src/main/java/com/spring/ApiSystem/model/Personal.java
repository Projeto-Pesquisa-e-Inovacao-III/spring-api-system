package com.spring.ApiSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Personal{
    @ManyToOne
    private Usuario usuario;

    @Column(unique = true, nullable = false)
    private String cref;

    public Personal() {
    }

    public Personal(Usuario usuario, String cref) {
        this.usuario = usuario;
        this.cref = cref;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }
}
