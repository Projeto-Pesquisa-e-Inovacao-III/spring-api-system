package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AnamneseController")
class AnamneseControllerTest {

    @Mock
    private AnamneseService anamneseService;

    @InjectMocks
    private AnamneseController anamneseController;

    private ReqCadastrarAnamneseDto reqCadastrar;
    private ReqAtualizarAnamneseDto reqAtualizar;

    private Anamnese anamnese;

    @BeforeEach
    void setUp() {
        anamnese = new Anamnese();
        anamnese.setAltura(1.75);
        anamnese.setPeso(70.0);
        anamnese.setObjectivoPrincipal("Hipertrofia");
        anamnese.setNivelDeAtividade(NivelDeAtividadeEnum.ATIVO);

        reqCadastrar = new ReqCadastrarAnamneseDto(
                1.75,
                70.0,
                "Hipertrofia",
                "Trabalho de escritório",
                List.of(),
                NivelDeAtividadeEnum.ATIVO,
                "Nenhuma"
        );

        reqAtualizar = new ReqAtualizarAnamneseDto(
                1.80,
                75.0,
                "Emagrecimento",
                null,
                List.of(),
                NivelDeAtividadeEnum.MUITO_ATIVO,
                null
        );
    }

    // =========================================================
    // POST /api/anamnese - cadastrarAnamnese
    // =========================================================

    @Test
    @DisplayName("POST /api/anamnese - Deve retornar 201 CREATED ao cadastrar anamnese")
    void deveRetornar201AoCadastrarAnamnese() {
        // Arrange
        when(anamneseService.cadastrarAnamnese(reqCadastrar)).thenReturn(anamnese);

        // Act
        ResponseEntity<Void> response = anamneseController.cadastrarAnamnese(reqCadastrar);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());

        verify(anamneseService, times(1)).cadastrarAnamnese(reqCadastrar);
    }

    @Test
    @DisplayName("POST /api/anamnese - Deve chamar o service exatamente uma vez ao cadastrar")
    void deveChamarServiceUmaVezAoCadastrar() {
        // Arrange
        when(anamneseService.cadastrarAnamnese(reqCadastrar)).thenReturn(anamnese);

        // Act
        anamneseController.cadastrarAnamnese(reqCadastrar);

        // Assert
        verify(anamneseService, times(1)).cadastrarAnamnese(reqCadastrar);
    }

    @Test
    @DisplayName("POST /api/anamnese - Deve propagar exceção do service ao cadastrar")
    void devePropagarExcecaoDoServiceAoCadastrar() {
        // Arrange
        doThrow(new RuntimeException("Erro inesperado"))
                .when(anamneseService).cadastrarAnamnese(reqCadastrar);

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> anamneseController.cadastrarAnamnese(reqCadastrar));
    }

    // =========================================================
    // PUT /api/anamnese - atualizarAnamnese
    // =========================================================

    @Test
    @DisplayName("PUT /api/anamnese - Deve retornar 204 NO CONTENT ao atualizar anamnese")
    void deveRetornar204AoAtualizarAnamnese() {
        // Arrange
        when(anamneseService.atualizarAnamnese(reqAtualizar)).thenReturn(anamnese);

        // Act
        ResponseEntity<Void> response = anamneseController.atualizarAnamnese(reqAtualizar);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(anamneseService, times(1)).atualizarAnamnese(reqAtualizar);
    }

    @Test
    @DisplayName("PUT /api/anamnese - Deve chamar o service exatamente uma vez ao atualizar")
    void deveChamarServiceUmaVezAoAtualizar() {
        // Arrange
        when(anamneseService.atualizarAnamnese(reqAtualizar)).thenReturn(anamnese);

        // Act
        anamneseController.atualizarAnamnese(reqAtualizar);

        // Assert
        verify(anamneseService, times(1)).atualizarAnamnese(reqAtualizar);
    }

    @Test
    @DisplayName("PUT /api/anamnese - Deve propagar exceção do service ao atualizar")
    void devePropagarExcecaoDoServiceAoAtualizar() {
        // Arrange
        when(anamneseService.atualizarAnamnese(reqAtualizar))
                .thenThrow(new RuntimeException("Anamnese não encontrada"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> anamneseController.atualizarAnamnese(reqAtualizar));
    }

    @Test
    @DisplayName("PUT /api/anamnese - O corpo da resposta deve ser nulo (no content)")
    void corpoRespostaDeveSerNuloAoAtualizar() {
        // Arrange
        when(anamneseService.atualizarAnamnese(reqAtualizar)).thenReturn(anamnese);

        // Act
        ResponseEntity<Void> response = anamneseController.atualizarAnamnese(reqAtualizar);

        // Assert
        assertNull(response.getBody());
    }
}
