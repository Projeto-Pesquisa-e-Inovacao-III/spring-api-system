package com.spring.ApiSystem.domain.aluno.vo;

import com.spring.ApiSystem.domain.aluno.vo.exception.CpfNuloOuVazioException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CpfConverter implements AttributeConverter<Cpf, String> {

    @Override
    public String convertToDatabaseColumn(Cpf cpf) {
        if (cpf == null) {
            throw new CpfNuloOuVazioException();
        }
        return cpf.getValue();
    }

    @Override
    public Cpf convertToEntityAttribute(String value) {
        if (value == null) {
            throw new CpfNuloOuVazioException();
        }
        return new Cpf(value);
    }
}
