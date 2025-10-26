package com.spring.ApiSystem.exception;

public class PersonalNaoExisteExcpetion extends RuntimeException {
    public PersonalNaoExisteExcpetion() {
        super("Personal com esse ID não existe " );
    }
}
