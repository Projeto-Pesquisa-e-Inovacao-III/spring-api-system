package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.response.ResAlunosPagantesDTO;
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

    @BeforeEach
    void setUp() {
        // Setup adicional se necessário
    }

    @Test
    @DisplayName("Deve retornar quantidade correta de alunos com planos ativos")
    void deveRetornarQuantidadeCorretaDeAlunosComPlanosAtivos() {
        // Arrange
        Integer quantidadeEsperada = 25;

        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(quantidadeEsperada, resultado.getQuantidadeAlunos());

        verify(alunoRepository, times(1)).countAlunosComPlanosAtivos();
    }

    @Test
    @DisplayName("Deve retornar 0 quando não há alunos com planos ativos")
    void deveRetornarZeroQuandoNaoHaAlunosComPlanosAtivos() {
        // Arrange
        Integer quantidadeEsperada = 0;

        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getQuantidadeAlunos());
    }

    @Test
    @DisplayName("Deve retornar 1 quando há apenas um aluno com plano ativo")
    void deveRetornarUmQuandoHaApenasUmAlunoComPlanoAtivo() {
        // Arrange
        Integer quantidadeEsperada = 1;

        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getQuantidadeAlunos());
    }

    @Test
    @DisplayName("Deve retornar quantidade grande de alunos com planos ativos")
    void deveRetornarQuantidadeGrandeDeAlunosComPlanosAtivos() {
        // Arrange
        Integer quantidadeEsperada = 500;

        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(quantidadeEsperada);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(500, resultado.getQuantidadeAlunos());
    }

    @Test
    @DisplayName("Deve chamar o repositório apenas uma vez")
    void deveChamarRepositorioApenasUmaVez() {
        // Arrange
        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(10);

        // Act
        alunoService.contarAlunosComPlanosAtivos();

        // Assert
        verify(alunoRepository, times(1)).countAlunosComPlanosAtivos();
        verifyNoMoreInteractions(alunoRepository);
    }

    @Test
    @DisplayName("Deve criar DTO com valor correto do repositório")
    void deveCriarDtoComValorCorretoDoRepositorio() {
        // Arrange
        Integer valorRepositorio = 42;

        when(alunoRepository.countAlunosComPlanosAtivos())
                .thenReturn(valorRepositorio);

        // Act
        ResAlunosPagantesDTO resultado = alunoService.contarAlunosComPlanosAtivos();

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getQuantidadeAlunos());
        assertEquals(valorRepositorio, resultado.getQuantidadeAlunos());
    }
}

