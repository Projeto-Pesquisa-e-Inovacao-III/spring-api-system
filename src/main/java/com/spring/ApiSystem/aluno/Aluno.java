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



    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
