package com.spring.ApiSystem.dto.usuario.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.util.Date;

public class EditarUsuarioDTO {
    @NotBlank(message = "O nome não pode ficar vazio ou nulo")
    private String nome;

    private String sexo;

    @Past(message = "A data de nascimento deve estar no passado")
    private Date dataNascimento;

    @Email(message = "Email deve ter formato válido")
    private String email;

    @NotBlank(message = "A senha deve ser válida")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    public EditarUsuarioDTO() {}

    public EditarUsuarioDTO(String nome, String email,
                            String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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
}