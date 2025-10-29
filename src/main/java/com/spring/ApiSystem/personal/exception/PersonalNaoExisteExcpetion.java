package com.spring.ApiSystem.personal.exception;

public class PersonalNaoExisteExcpetion extends RuntimeException {
    public PersonalNaoExisteExcpetion() {
        super("Personal com esse ID não existe " );
    }
}
