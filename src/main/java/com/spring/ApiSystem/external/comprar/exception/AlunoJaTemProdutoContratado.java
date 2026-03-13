package com.spring.ApiSystem.external.comprar.exception;

public class AlunoJaTemProdutoContratado extends RuntimeException {
    public AlunoJaTemProdutoContratado() {
        super("O aluno já tem um produto contratado do tipo pacote");
    }
}
