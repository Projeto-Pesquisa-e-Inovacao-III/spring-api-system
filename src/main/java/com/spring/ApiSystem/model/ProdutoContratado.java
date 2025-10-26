package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "produto_contratado")
public class ProdutoContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean ativo;

    @Column(name = "data_compra",
            nullable = false,
            updatable = false)
    private LocalDateTime dataCompra;

    @Column(name = "data_expiracao",
            nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(name = "saldo_aula",
            nullable = false)
    private Integer saldoAula;

    @ManyToOne
    @JoinColumn(name = "usuario_aluno_id",
                nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "produto_exibicao_id",
                nullable = false)
    private ProdutoExibicao produtoExibicao;


    public ProdutoContratado() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Integer getSaldoAula() {
        return saldoAula;
    }

    public void setSaldoAula(Integer saldoAula) {
        this.saldoAula = saldoAula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public ProdutoExibicao getProdutoExibicao() {
        return produtoExibicao;
    }

    public void setProdutoExibicao(ProdutoExibicao produtoExibicao) {
        this.produtoExibicao = produtoExibicao;
    }
}
