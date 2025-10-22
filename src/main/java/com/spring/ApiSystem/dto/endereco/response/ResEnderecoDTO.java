package com.spring.ApiSystem.dto.endereco.response;

import com.spring.ApiSystem.model.CEP;
import com.spring.ApiSystem.model.Usuario;

public class ResEnderecoDTO {
    private Long id;
    private String numero;
    private String complemento;
    private String unidade;
    private String tipo;
    private CEP cep;

    public ResEnderecoDTO() {
    }

    public ResEnderecoDTO(Long id, String numero,
                          String complemento, String unidade,
                          String tipo, CEP cep) {
        this.id = id;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.tipo = tipo;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public CEP getCep() {
        return cep;
    }

    public void setCep(CEP cep) {
        this.cep = cep;
    }
}
