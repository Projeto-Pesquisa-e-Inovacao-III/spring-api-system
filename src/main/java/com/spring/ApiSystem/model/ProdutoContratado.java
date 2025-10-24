package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "produto_contratado")
public class ProdutoContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String situacao;

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

    @JoinColumn(nullable = false)
    @ManyToOne
    private Aluno aluno;

    @JoinColumn(nullable = false)
    @ManyToOne
    private ProdutoExibicao produtoExibicao;


    public ProdutoContratado() {
    }


}
