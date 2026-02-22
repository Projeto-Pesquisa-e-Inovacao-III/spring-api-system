package com.spring.ApiSystem.domain.produtocontratado.exception;

public class UsuarioSemTipoAulaException extends RuntimeException {
    public UsuarioSemTipoAulaException() {
        super("Usuário não possui pacotes com o tipo de aula especificado.");
    }
}
