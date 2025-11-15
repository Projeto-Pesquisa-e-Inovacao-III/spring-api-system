package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.enums.TipoUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "aluno")
@PrimaryKeyJoinColumn(name = "id")
public class Aluno extends Usuario {
    @Column(unique = true)
    private String cpf;

    public Aluno() {
    }

    public Aluno(Long id, String nome, String sexo, LocalDate dataNascimento, String email, String salt, String senha, boolean ativo, String cpf) {
        super(id, TipoUsuario.ALUNO, nome, sexo, dataNascimento, email, salt, senha, ativo);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
