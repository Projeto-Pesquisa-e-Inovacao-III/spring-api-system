package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.response.ResAlunosPagantesDTO;
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
@DisplayName("Testes do AlunoController - Endpoint de Alunos Ativos")
class AlunoControllerTest {

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private AlunoController alunoController;

    @BeforeEach
    void setUp() {
        // Setup adicional se necessário
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar 200 OK com dados")
    void deveRetornar200ComDadosDeAlunosAtivos() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(25);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(25, response.getBody().getQuantidadeAlunos());

        verify(alunoService, times(1)).contarAlunosComPlanosAtivos();
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar 0 alunos")
    void deveRetornarZeroAlunos() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(0);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody().getQuantidadeAlunos());
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar 1 aluno")
    void deveRetornarUmAluno() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(1);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getQuantidadeAlunos());
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar grande quantidade de alunos")
    void deveRetornarGrandeQuantidadeDeAlunos() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(500);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(500, response.getBody().getQuantidadeAlunos());
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve chamar o service uma vez")
    void deveChamarServiceUmaVez() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(10);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        alunoController.buscarAlunosAtivos();

        // Assert
        verify(alunoService, times(1)).contarAlunosComPlanosAtivos();
        verifyNoMoreInteractions(alunoService);
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar corpo não nulo")
    void deveRetornarCorpoNaoNulo() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(15);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getQuantidadeAlunos());
    }

    @Test
    @DisplayName("GET /alunos/alunos-ativos - Deve retornar ResponseEntity OK")
    void deveRetornarResponseEntityOk() {
        // Arrange
        ResAlunosPagantesDTO responseDto = new ResAlunosPagantesDTO(30);

        when(alunoService.contarAlunosComPlanosAtivos())
                .thenReturn(responseDto);

        // Act
        ResponseEntity<ResAlunosPagantesDTO> response =
                alunoController.buscarAlunosAtivos();

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(200, response.getStatusCode().value());
    }
}

