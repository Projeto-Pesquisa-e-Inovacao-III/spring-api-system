package com.spring.ApiSystem.domain.personal.exception;

public class PersonalNaoExisteExcepetion extends RuntimeException {
    public PersonalNaoExisteExcepetion() {
        super("Personal com esse ID não existe " );
    }
}
