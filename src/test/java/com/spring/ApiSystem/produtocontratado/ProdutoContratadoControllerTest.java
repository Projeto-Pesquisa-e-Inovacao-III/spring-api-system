package com.spring.ApiSystem.produtocontratado;

import com.spring.ApiSystem.produtocontratado.dto.response.ResQuantidadePercentualAlunosExpiradosDto;
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

    @BeforeEach
    void setUp() {
        // Setup adicional se necessário
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-e-percentual-alunos-expirados - Deve retornar 200 OK com dados")
    void deveRetornar200ComDadosDeAlunosExpirados() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(12, 18.46);

        when(produtoContratadoService.contarEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contarEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(12, response.getBody().getQuantidadeAlunos());
        assertEquals(18.46, response.getBody().getPercentualAlunos());

        verify(produtoContratadoService, times(1)).contarEPercentualAlunosExpirados();
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-e-percentual-alunos-expirados - Deve retornar percentual 0.0")
    void deveRetornarPercentualZero() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(0, 0.0);

        when(produtoContratadoService.contarEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contarEPercentualAlunosExpirados();

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

        when(produtoContratadoService.contarEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contarEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(50, response.getBody().getQuantidadeAlunos());
        assertEquals(100.0, response.getBody().getPercentualAlunos());
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-e-percentual-alunos-expirados - Deve chamar o service uma vez")
    void deveChamarServiceUmaVez() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(5, 25.0);

        when(produtoContratadoService.contarEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        produtoContratadoController.contarEPercentualAlunosExpirados();

        // Assert
        verify(produtoContratadoService, times(1)).contarEPercentualAlunosExpirados();
        verifyNoMoreInteractions(produtoContratadoService);
    }

    @Test
    @DisplayName("GET /produtos-contratados/quantidade-e-percentual-alunos-expirados - Deve retornar corpo não nulo")
    void deveRetornarCorpoNaoNulo() {
        // Arrange
        ResQuantidadePercentualAlunosExpiradosDto responseDto =
                new ResQuantidadePercentualAlunosExpiradosDto(3, 7.5);

        when(produtoContratadoService.contarEPercentualAlunosExpirados())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResQuantidadePercentualAlunosExpiradosDto> response =
                produtoContratadoController.contarEPercentualAlunosExpirados();

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getQuantidadeAlunos());
        assertNotNull(response.getBody().getPercentualAlunos());
    }
}

