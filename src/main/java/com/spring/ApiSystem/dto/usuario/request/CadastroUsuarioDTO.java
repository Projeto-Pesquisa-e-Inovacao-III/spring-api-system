package com.spring.ApiSystem.dto.usuario.request;

import jakarta.validation.constraints.*;

import java.util.Date;

public class CadastroUsuarioDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String sexo;

    @Past(message = "A data de nascimento deve estar no passado")
    private Date dataNascimento;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    public CadastroUsuarioDTO() {}

    public CadastroUsuarioDTO(String nome, String sexo,
                              Date dataNascimento, String email,
                              String senha) {
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
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