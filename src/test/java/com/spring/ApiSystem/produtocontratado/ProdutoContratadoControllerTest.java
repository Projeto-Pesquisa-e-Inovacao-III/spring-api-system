package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoController;
import com.spring.ApiSystem.domain.produtocontratado.ProdutoContratadoService;
import com.spring.ApiSystem.domain.produtocontratado.dto.response.ResQuantidadePercentualAlunosExpiradosDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ProdutoContratadoController - Endpoint de Alunos Expirados")
class ProdutoContratadoControllerTest {

    @Mock
    private ProdutoContratadoService produtoContratadoService;

    @InjectMocks
    private ProdutoContratadoController produtoContratadoController;

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-percentual-alunos-expirados - Deve retornar 200 OK com dados")
    void deveRetornar200ComDadosDeAlunosExpirados() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(12, 18.46);

        when(produtoContratadoService.contagemEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(12, response.getBody().getQuantidadeAlunos());
        assertEquals(18.46, response.getBody().getPercentualAlunos());

        verify(produtoContratadoService, times(1)).contagemEPercentualAlunosExpirados();
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-percentual-alunos-expirados - Deve retornar percentual 0.0")
    void deveRetornarPercentualZero() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(0, 0.0);

        when(produtoContratadoService.contagemEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody().getQuantidadeAlunos());
        assertEquals(0.0, response.getBody().getPercentualAlunos());
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-e-percentual-alunos-expirados - Deve retornar percentual 100.0")
    void deveRetornarPercentual100() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(50, 100.0);

        when(produtoContratadoService.contagemEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(50, response.getBody().getQuantidadeAlunos());
        assertEquals(100.0, response.getBody().getPercentualAlunos());
    }


    @Test
    @DisplayName("GET /produtos-contratados/quantidade-percentual-alunos-expirados - Deve retornar corpo não nulo")
    void deveRetornarCorpoNaoNulo() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(3, 7.5);

        when(produtoContratadoService.contagemEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contagemEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getQuantidadeAlunos());
        assertNotNull(response.getBody().getPercentualAlunos());
    }
}

