package com.spring.ApiSystem.dto.endereco.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EnderecoDTO {
    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 8, message = "CEP deve ter 8 caracteres")
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 100, message = "Logradouro pode ter no máximo 100 caracteres")
    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    private String numero;

    @Size(max = 45, message = "Complemento pode ter no máximo 45 caracteres")
    private String complemento;

    @Size(max = 45, message = "Unidade pode ter no máximo 45 caracteres")
    private String unidade;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 45, message = "Bairro pode ter no máximo 45 caracteres")
    private String bairro;

    @NotBlank(message = "Localidade é obrigatória")
    @Size(max = 45, message = "Localidade pode ter no máximo 45 caracteres")
    private String localidade;

    @NotBlank(message = "UF é obrigatória")
    @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres")
    private String uf;

    @Size(max = 255, message = "Descrição pode ter no máximo 255 caracteres")
    private String descricao;


    public EnderecoDTO() {
    }

    public EnderecoDTO(String cep, String logradouro, String numero,
                       String complemento, String unidade, String bairro,
                       String localidade, String uf, String descricao) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.descricao = descricao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
