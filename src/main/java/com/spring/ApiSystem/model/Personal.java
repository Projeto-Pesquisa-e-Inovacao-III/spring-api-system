package com.spring.ApiSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity(name = "personal")
@DiscriminatorValue("Personal")
public class Personal extends Usuario{
    @Column(unique = true)
    private String cref;

    public Personal() {
    }

    public Personal(Long id, String nome,
                    String sexo, Date dataNascimento,
                    String email, String salt,
                    String senha, boolean ativo,
                    String cref) {
        super(id, nome, sexo, dataNascimento, email, salt, senha, ativo);
        this.cref = cref;
    }

    public String getCref() {
        return cref;
    }

    public void setCref(String cref) {
        this.cref = cref;
    }

    @Override
    public String toString() {
        return "Personal{" +
                "cref='" + cref + '\'' +
                '}';
    }
}
