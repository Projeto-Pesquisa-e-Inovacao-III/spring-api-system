package com.spring.ApiSystem.domain.aluno;


import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.enums.TipoUsuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.time.Period;

@Entity
@Table(name = "aluno")
@PrimaryKeyJoinColumn(name = "id")
public class Aluno extends Usuario {

    @Column(unique = true)
    private String cpf;

    public Aluno() {
    }

    public Aluno(Long id, TipoUsuario tipo, String nome, String sexo, LocalDate dataNascimento, String email, String salt, String senha, boolean ativo, String caminhoFoto, List<Telefone> telefones, String cpf) {
        super(id, TipoUsuario.ALUNO, nome, sexo, dataNascimento, email, salt, senha, ativo, caminhoFoto, telefones);
        this.cpf = cpf;
    }
    public Integer getIdade() {
        return Period.between(getDataNascimento(), LocalDate.now()).getYears();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


}
