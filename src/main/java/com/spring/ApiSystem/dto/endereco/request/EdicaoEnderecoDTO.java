package com.spring.ApiSystem.dto.endereco.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EdicaoEnderecoDTO {
    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 8, message = "CEP deve ter 8 caracteres")
    private String cep;

    @NotBlank(message = "Número é obrigatório")
    private String numero;

    @Size(max = 45, message = "Complemento pode ter no máximo 45 caracteres")
    private String complemento;

    @Size(max = 45, message = "Unidade pode ter no máximo 45 caracteres")
    private String unidade;

    @Size(max = 255, message = "Descrição pode ter no máximo 255 caracteres")
    private String descricao;

    public EdicaoEnderecoDTO() {
    }

    public EdicaoEnderecoDTO(String cep, String numero,
                             String complemento, String unidade,
                             String descricao) {
        this.cep = cep;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.descricao = descricao;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
