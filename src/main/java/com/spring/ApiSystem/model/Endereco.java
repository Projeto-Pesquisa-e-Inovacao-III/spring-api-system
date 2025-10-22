package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    private String complemento;

    @Column(nullable = false)
    private String unidade;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime data_criacao;

    private LocalDateTime data_atualizacao;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private CEP cep;

    public Endereco() {
    }

    public Endereco(Long id, String numero,
                    String complemento, String unidade,
                    String tipo, LocalDateTime data_criacao,
                    LocalDateTime data_atualizacao,
                    Usuario usuario, CEP cep) {
        this.id = id;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.tipo = tipo;
        this.data_criacao = data_criacao;
        this.data_atualizacao = data_atualizacao;
        this.usuario = usuario;
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

    public LocalDateTime getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDateTime data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDateTime getData_atualizacao() {
        return data_atualizacao;
    }

    public void setData_atualizacao(LocalDateTime data_atualizacao) {
        this.data_atualizacao = data_atualizacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CEP getCep() {
        return cep;
    }

    public void setCep(CEP cep) {
        this.cep = cep;
    }
}
