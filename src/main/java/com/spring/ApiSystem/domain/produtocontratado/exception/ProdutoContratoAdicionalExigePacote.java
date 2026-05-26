package com.spring.ApiSystem.domain.produtocontratado.exception;

public class ProdutoContratoAdicionalExigePacote extends RuntimeException {
    public ProdutoContratoAdicionalExigePacote() {
        super("Um produto adicional só pode ser contratado quando existir um produto pacote vinculado ao contrato.");
    }
}
