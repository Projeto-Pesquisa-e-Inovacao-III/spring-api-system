package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "produto_contratado")
public class ProdutoContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean ativo;

    @Column(name = "data_compra",
            nullable = false,
            updatable = false)
    private Date dataCompra;

    @Column(name = "data_expiracao",
            nullable = false)
    private Date dataExpiracao;

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


}
