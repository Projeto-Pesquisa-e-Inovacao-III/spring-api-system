package com.spring.ApiSystem.dto.usuario.response;

import java.util.Date;

public class ResUsuarioDTO {

    private Long id;
    private String nome;
    private String sexo;
    private Date dataNascimento;
    private String email;
    private boolean ativo;

    public ResUsuarioDTO() {
    }

    public ResUsuarioDTO(Long id, String nome, String sexo,
                         Date dataNascimento, String email, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
