package com.spring.ApiSystem.produtocontratado.exception;

public class UsuarioSemTipoAulaException extends RuntimeException {
    public UsuarioSemTipoAulaException() {
        super("Usuário não possui pacotes com o tipo de aula especificado.");
    }
}
