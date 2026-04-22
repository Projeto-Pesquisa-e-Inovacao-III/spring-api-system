package com.spring.ApiSystem.domain.aluno;


import com.spring.ApiSystem.domain.aluno.AlunoRepository;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.dto.response.ResAlunosPagantesDTO;
import com.spring.ApiSystem.domain.produtoexibicao.enums.TipoProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AlunoService - Alunos com Planos Ativos")
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    @DisplayName("Deve retornar quantidade correta de alunos com planos ativos")
    void deveRetornarQuantidadeCorretaDeAlunosComPlanosAtivos() {
        // Arrange
        Integer quantidadeEsperada = 25;

        when(alunoRepository.countAlunosComPlanosAtivos(TipoProduto.PACOTE))
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(quantidadeEsperada, resultado.getQuantidadeAlunos());

        verify(alunoRepository, times(1)).countAlunosComPlanosAtivos(TipoProduto.PACOTE);
    }

    @Test
    @DisplayName("Deve retornar 0 quando não há alunos com planos ativos")
    void deveRetornarZeroQuandoNaoHaAlunosComPlanosAtivos() {
        // Arrange
        Integer quantidadeEsperada = 0;

        when(alunoRepository.countAlunosComPlanosAtivos(TipoProduto.PACOTE))
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getQuantidadeAlunos());
    }


    @Test
    @DisplayName("Deve criar DTO com valor correto do repositório")
    void deveCriarDtoComValorCorretoDoRepositorio() {
        // Arrange
        Integer valorRepositorio = 42;

        when(alunoRepository.countAlunosComPlanosAtivos(TipoProduto.PACOTE))
                .thenReturn(valorRepositorio);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getQuantidadeAlunos());
        assertEquals(valorRepositorio, resultado.getQuantidadeAlunos());
    }
}

