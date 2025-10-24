package com.spring.ApiSystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArgonService {
    private final Argon2PasswordEncoder argon;

    public ArgonService(Argon2PasswordEncoder argon) {
        this.argon = argon;
    }

    public List<String> criptografarSenha(String senha) {
        String senhaCriptografada = argon.encode(senha);
        String[] senhaDividida = senhaCriptografada.split("\\$");
        return List.of(senhaDividida[4], senhaDividida[5]);
    }

    @Value("${argon.algoritmo}")
    String algoritmo;

    @Value("${argon.versao}")
    String versao;

    @Value("${argon.memory}")
    String memory;

    @Value("${argon.iterations}")
    String iterations;

    @Value("${argon.parallelism}")
    String parallelism;

    public boolean validarSenha(String senhaDigitada, String salt, String senhaHash) {
        String senhaCompleta = "$"   + algoritmo   +
                               "$v=" + versao      +
                               "$m=" + memory      + "," +
                               "t="  + iterations  + "," +
                               "p="  + parallelism +
                               "$"   + salt +
                               "$"   + senhaHash;
        return argon.matches(senhaDigitada, senhaCompleta);
    }
}
