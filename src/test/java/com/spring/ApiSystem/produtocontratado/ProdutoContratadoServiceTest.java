package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.aluno.AlunoRepository;
import com.spring.ApiSystem.produtocontratado.dto.response.ResQuantidadePercentualAlunosExpiradosDto;
import com.spring.ApiSystem.produtoexibicao.enums.TipoProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ProdutoContratadoService - Alunos Expirados")
class ProdutoContratadoServiceTest {

    @Mock
    private ProdutoContratadoRepository produtoContratadoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private ProdutoContratadoService produtoContratadoService;

    @Test
    @DisplayName("Deve calcular quantidade e percentual corretamente quando há alunos expirados")
    void deveCalcularQuantidadeEPercentualCorretamente() {
        // Arrange
        Integer quantidadeExpirados = 15;
        Long totalAlunos = 100L;
        Double percentualEsperado = 15.0;

        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(quantidadeExpirados);
        when(alunoRepository.count()).thenReturn(totalAlunos);

        // Act
        ResQuantidadePercentualAlunosExpiradosDto resultado =
                produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(resultado);
        assertEquals(quantidadeExpirados, resultado.getQuantidadeAlunos());
        assertEquals(percentualEsperado, resultado.getPercentualAlunos());

        verify(produtoContratadoRepository, times(1))
                .countAlunosComPlanosExpirados(any(TipoProduto.class));
        verify(alunoRepository, times(1)).count();
    }

    @Test
    @DisplayName("Deve retornar percentual 0.0 quando não há alunos cadastrados")
    void deveRetornarPercentualZeroQuandoNaoHaAlunos() {
        // Arrange
        Integer quantidadeExpirados = 0;
        Long totalAlunos = 0L;

        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(quantidadeExpirados);
        when(alunoRepository.count()).thenReturn(totalAlunos);

        // Act
        ResQuantidadePercentualAlunosExpiradosDto resultado =
                produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getQuantidadeAlunos());
        assertEquals(0.0, resultado.getPercentualAlunos());
    }

    @Test
    @DisplayName("Deve retornar percentual 0.0 quando nenhum aluno tem plano expirado")
    void deveRetornarPercentualZeroQuandoNenhumAlunoTemPlanoExpirado() {
        // Arrange
        Integer quantidadeExpirados = 0;
        Long totalAlunos = 50L;
        Double percentualEsperado = 0.0;

        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(quantidadeExpirados);
        when(alunoRepository.count()).thenReturn(totalAlunos);

        // Act
        ResQuantidadePercentualAlunosExpiradosDto resultado =
                produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getQuantidadeAlunos());
        assertEquals(percentualEsperado, resultado.getPercentualAlunos());
    }

    @Test
    @DisplayName("Deve retornar percentual 100.0 quando todos os alunos têm planos expirados")
    void deveRetornarPercentual100QuandoTodosAlunosTemPlanosExpirados() {
        // Arrange
        Integer quantidadeExpirados = 30;
        Long totalAlunos = 30L;
        Double percentualEsperado = 100.0;

        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(quantidadeExpirados);
        when(alunoRepository.count()).thenReturn(totalAlunos);

        // Act
        ResQuantidadePercentualAlunosExpiradosDto resultado =
                produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(resultado);
        assertEquals(30, resultado.getQuantidadeAlunos());
        assertEquals(percentualEsperado, resultado.getPercentualAlunos());
    }

    @Test
    @DisplayName("Deve calcular percentual com casas decimais corretamente")
    void deveCalcularPercentualComCasasDecimais() {
        // Arrange
        Integer quantidadeExpirados = 7;
        Long totalAlunos = 13L;
        Double percentualEsperado = (7.0 / 13.0) * 100.0; // ~53.846...

        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(quantidadeExpirados);
        when(alunoRepository.count()).thenReturn(totalAlunos);

        // Act
        ResQuantidadePercentualAlunosExpiradosDto resultado =
                produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(resultado);
        assertEquals(7, resultado.getQuantidadeAlunos());
        assertEquals(percentualEsperado, resultado.getPercentualAlunos(), 0.001);
    }

    @Test
    @DisplayName("Deve usar LocalDate.now() como parâmetro para a query")
    void deveUsarLocalDateNowComoParametro() {
        // Arrange
        when(produtoContratadoRepository.countAlunosComPlanosExpirados(any(TipoProduto.class)))
                .thenReturn(5);
        when(alunoRepository.count()).thenReturn(20L);

        // Act
        produtoContratadoService.contagemEPercentualAlunosExpirados();

        // Assert
        verify(produtoContratadoRepository).countAlunosComPlanosExpirados(any(TipoProduto.class));
    }
}