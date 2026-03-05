package com.spring.ApiSystem.domain.aluno.vo;

import java.util.Objects;


import com.spring.ApiSystem.domain.aluno.exception.CpfInvalidoException;
import com.spring.ApiSystem.domain.aluno.vo.exception.CpfDigitosIguaisException;
import com.spring.ApiSystem.domain.aluno.vo.exception.CpfNuloOuVazioException;
import com.spring.ApiSystem.domain.aluno.vo.exception.CpfTamanhoInvalidoException;

public final class Cpf {

    private final String value;

    public Cpf(String cpf) {
        String normalized = normalize(cpf);
        validate(normalized);
        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    private String normalize(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new CpfNuloOuVazioException();
        }

        return cpf.replaceAll("\\D", "");
    }


    private void validate(String cpf) {

        if (cpf.length() != 11) {
            throw new CpfTamanhoInvalidoException();
        }

        if (allDigitsEqual(cpf)) {
            throw new CpfDigitosIguaisException();
        }

        int digit1 = calculateDigit(cpf, 9, 10);
        int digit2 = calculateDigit(cpf, 10, 11);

        int informedDigit1 = cpf.charAt(9) - '0';
        int informedDigit2 = cpf.charAt(10) - '0';

        if (digit1 != informedDigit1 || digit2 != informedDigit2) {
            throw new CpfInvalidoException();
        }
    }

    private boolean allDigitsEqual(String cpf) {

        char first = cpf.charAt(0);

        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != first) {
                return false;
            }
        }

        return true;
    }

    private int calculateDigit(String cpf, int length, int weightStart) {

        int sum = 0;
        int weight = weightStart;

        for (int i = 0; i < length; i++) {

            int num = cpf.charAt(i) - '0';

            sum += num * weight;
            weight--;
        }

        int remainder = sum % 11;

        return remainder < 2 ? 0 : 11 - remainder;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Cpf)) return false;

        Cpf cpf = (Cpf) o;

        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public String formatted() {

        return value.substring(0,3) + "." +
                value.substring(3,6) + "." +
                value.substring(6,9) + "-" +
                value.substring(9);
    }

    @Override
    public String toString() {
        return formatted();
    }
}