package com.spring.ApiSystem.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ArgonService {
    private final Argon2PasswordEncoder argon;

    public ArgonService(Argon2PasswordEncoder argon) {
        this.argon = argon;
    }

    public String criptografarSenha(String senha) {
        return argon.encode(senha);
    }

    public boolean validarSenha(String senhaDigitada, String hashSalvo) {
        return argon.matches(senhaDigitada, hashSalvo);
    }

}
