package com.spring.ApiSystem.domain.aluno.vo;

import com.spring.ApiSystem.domain.aluno.vo.exception.CpfDigitosIguaisException;
import com.spring.ApiSystem.domain.aluno.vo.exception.CpfNuloOuVazioException;
import com.spring.ApiSystem.domain.aluno.vo.exception.CpfTamanhoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CpfTest {

    @Test
    void deveNormalizarECriarCpfValidoAPartirDeFormatoComPontosEHifen() {
        Cpf cpf = new Cpf("123.456.789-09"); // CPF de exemplo válido
        assertThat(cpf.getValue()).isEqualTo("12345678909");
        assertThat(cpf.formatted()).isEqualTo("123.456.789-09");
        assertThat(cpf.toString()).isEqualTo("123.456.789-09");
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNuloOuBranco() {
        assertThrows(CpfNuloOuVazioException.class, () -> new Cpf(null));
        assertThrows(CpfNuloOuVazioException.class, () -> new Cpf("   "));
    }

    @Test
    void deveLancarExcecaoQuandoTodosOsDigitosForemIguais() {
        assertThrows(CpfDigitosIguaisException.class, () -> new Cpf("111.111.111-11"));
    }

    @Test
    void deveLancarExcecaoQuandoContemLetrasOuFicarComTamanhoInvalidoAposNormalizacao() {
        // contém letras -> após remover não dígitos o resultado terá tamanho != 11
        assertThrows(CpfTamanhoInvalidoException.class, () -> new Cpf("12345A67890"));
        assertThrows(CpfTamanhoInvalidoException.class, () -> new Cpf("abc.def.ghi-jk"));
    }
}
