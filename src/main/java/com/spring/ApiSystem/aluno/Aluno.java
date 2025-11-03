package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity(name = "aluno")
@DiscriminatorValue("Aluno")
public class Aluno extends Usuario {
    @Column(unique = true)
    private String cpf;

    public Aluno() {
    }

    public Aluno(Long id, String tipo, String nome, String sexo, LocalDate dataNascimento,
                 String email, String salt, String senha, boolean ativo, String cpf) {
        super(id, tipo, nome, sexo, dataNascimento, email, salt, senha, ativo);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
