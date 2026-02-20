package com.spring.ApiSystem.domain.produtocontratado;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.produtoexibicao.ProdutoExibicao;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "produto_contratado")
public class ProdutoContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean situacao;

    @Column(name = "data_compra",
            nullable = false,
            updatable = false)
    private LocalDate dataCompra;

    @Column(name = "data_expiracao",
            nullable = false)
    private LocalDate dataExpiracao;

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

    public ProdutoContratado(Long id, Boolean situacao,
                             LocalDate dataCompra, LocalDate dataExpiracao,
                             Integer saldoAula, Aluno aluno,
                             ProdutoExibicao produtoExibicao) {
        this.id = id;
        this.situacao = situacao;
        this.dataCompra = dataCompra;
        this.dataExpiracao = dataExpiracao;
        this.saldoAula = saldoAula;
        this.aluno = aluno;
        this.produtoExibicao = produtoExibicao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSituacao() {
        return situacao;
    }

    public void setSituacao(Boolean situacao) {
        this.situacao = situacao;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
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
