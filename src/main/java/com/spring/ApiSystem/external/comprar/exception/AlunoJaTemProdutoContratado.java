package com.spring.ApiSystem.domain.comprar.exception;

public class AlunoJaTemProdutoContratado extends RuntimeException {
    public AlunoJaTemProdutoContratado() {
        super("O Aluno já tem um produto Contratado");
    }
}
