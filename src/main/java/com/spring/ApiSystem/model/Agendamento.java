package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private String situacao;

    private String descricao;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Endereco endereco;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Aluno aluno;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Personal personal;

    @JoinColumn(nullable = false)
    @ManyToOne
    private ProdutoContratado produtoContratado;

    public Agendamento() {
    }

    public Agendamento(Long id, LocalDateTime data,
                       String situacao, String descricao,
                       Endereco endereco, Aluno aluno,
                       Personal personal,
                       ProdutoContratado produtoContratado) {
        this.id = id;
        this.data = data;
        this.situacao = situacao;
        this.descricao = descricao;
        this.endereco = endereco;
        this.aluno = aluno;
        this.personal = personal;
        this.produtoContratado = produtoContratado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public ProdutoContratado getProdutoContratado() {
        return produtoContratado;
    }

    public void setProdutoContratado(ProdutoContratado produtoContratado) {
        this.produtoContratado = produtoContratado;
    }
}
