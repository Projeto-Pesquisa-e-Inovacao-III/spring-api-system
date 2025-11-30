package com.spring.ApiSystem.personal.exception;

public class PersonalNaoExisteExcepetion extends RuntimeException {
    public PersonalNaoExisteExcepetion() {
        super("Personal com esse ID não existe " );
    }
}
