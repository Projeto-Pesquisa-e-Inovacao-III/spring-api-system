package com.spring.ApiSystem.domain.cep;

import jakarta.persistence.*;

@Entity
public class CEP {
    @Id
    @Column(unique = true)
    private String id;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    public CEP() {
    }

    public CEP(String id, String logradouro,
               String bairro, String localidade,
               String uf) {
        this.id = id;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
