package com.spring.ApiSystem.comprar.exception;

public class AlunoJaTemProdutoContratado extends RuntimeException {
    public AlunoJaTemProdutoContratado() {
        super("O Aluno já tem um produto Contratado");
    }
}
