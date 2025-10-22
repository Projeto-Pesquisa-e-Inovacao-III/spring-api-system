package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ProdutoContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String situacao;

    @Column(nullable = false, updatable = false)
    private Date dataCompra;

    @Column(nullable = false)
    private Date dataExpiracao;

    @Column(nullable = false)
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
