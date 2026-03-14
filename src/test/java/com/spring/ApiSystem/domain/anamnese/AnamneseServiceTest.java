package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.enums.NivelDeAtividadeEnum;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseJaExisteException;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseNaoEncontradaException;
import com.spring.ApiSystem.domain.anamnese.mapper.AnamneseMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AnamneseService")
class AnamneseServiceTest {

    @Mock
    private AnamneseRepository anamneseRepository;

    @Mock
    private JpaUserDetailsService jpaUserDetailsService;

    @Mock
    private AlunoService alunoService;

    @Mock
    private AnamneseMapper anamneseMapper;

    @InjectMocks
    private AnamneseService anamneseService;

    private Aluno alunoSemAnamnese;
    private Aluno alunoComAnamnese;
    private Anamnese anamnese;
    private ReqCadastrarAnamneseDto reqCadastrar;
    private ReqAtualizarAnamneseDto reqAtualizar;

    @BeforeEach
    void setUp() {
        anamnese = new Anamnese();
        anamnese.setAltura(1.75);
        anamnese.setPeso(70.0);
        anamnese.setObjectivoPrincipal("Hipertrofia");
        anamnese.setRotina("Trabalho de escritório");
        anamnese.setNivelDeAtividade(NivelDeAtividadeEnum.ATIVO);
        anamnese.setObservacaoSaude("Nenhuma");
        anamnese.setCondicoes(List.of());

        alunoSemAnamnese = new Aluno();
        alunoSemAnamnese.setAtivoAnamnese(false);

        alunoComAnamnese = new Aluno();
        alunoComAnamnese.setAtivoAnamnese(true);
        alunoComAnamnese.setAnamnese(anamnese);

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
    // cadastrarAnamnese
    // =========================================================

    @Test
    @DisplayName("Deve cadastrar anamnese com sucesso quando aluno não tem anamnese")
    void deveCadastrarAnamnesComSucesso() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoSemAnamnese);
        when(anamneseMapper.toEntityFromRequest(reqCadastrar)).thenReturn(anamnese);
        when(alunoService.registrarAnamnese(alunoSemAnamnese, anamnese)).thenReturn(alunoComAnamnese);

        // Act
        Anamnese resultado = anamneseService.cadastrarAnamnese(reqCadastrar);

        // Assert
        assertNotNull(resultado);
        assertEquals(anamnese, resultado);
        verify(jpaUserDetailsService, times(1)).getCurrentAluno();
        verify(anamneseMapper, times(1)).toEntityFromRequest(reqCadastrar);
        verify(alunoService, times(1)).registrarAnamnese(alunoSemAnamnese, anamnese);
    }

    @Test
    @DisplayName("Deve lançar AnamneseJaExisteException quando aluno já possui anamnese")
    void deveLancarExcecaoQuandoAlunoJaPossuiAnamnese() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoComAnamnese);

        // Act & Assert
        assertThrows(AnamneseJaExisteException.class,
                () -> anamneseService.cadastrarAnamnese(reqCadastrar));

        verify(anamneseMapper, never()).toEntityFromRequest(any());
        verify(alunoService, never()).registrarAnamnese(any(), any());
    }

    @Test
    @DisplayName("Não deve chamar o mapper quando aluno já possui anamnese")
    void naoDeveChamarMapperQuandoAlunoJaPossuiAnamnese() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoComAnamnese);

        // Act & Assert
        assertThrows(AnamneseJaExisteException.class,
                () -> anamneseService.cadastrarAnamnese(reqCadastrar));

        verifyNoInteractions(anamneseMapper);
    }

    // =========================================================
    // atualizarAnamnese
    // =========================================================

    @Test
    @DisplayName("Deve atualizar anamnese com sucesso quando aluno possui anamnese")
    void deveAtualizarAnamneseComSucesso() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoComAnamnese);
        when(anamneseRepository.save(anamnese)).thenReturn(anamnese);

        // Act
        Anamnese resultado = anamneseService.atualizarAnamnese(reqAtualizar);

        // Assert
        assertNotNull(resultado);
        verify(anamneseMapper, times(1)).updateEntityFromRequest(reqAtualizar, anamnese);
        verify(anamneseRepository, times(1)).save(anamnese);
    }

    @Test
    @DisplayName("Deve lançar AnamneseNaoEncontradaException quando aluno não possui anamnese")
    void deveLancarExcecaoQuandoAlunoNaoPossuiAnamnese() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoSemAnamnese);

        // Act & Assert
        assertThrows(AnamneseNaoEncontradaException.class,
                () -> anamneseService.atualizarAnamnese(reqAtualizar));

        verify(anamneseMapper, never()).updateEntityFromRequest(any(), any());
        verify(anamneseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve chamar repository quando a anamnese não é encontrada")
    void naoDeveSalvarQuandoAnamneseNaoEncontrada() {
        // Arrange
        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoSemAnamnese);

        // Act & Assert
        assertThrows(AnamneseNaoEncontradaException.class,
                () -> anamneseService.atualizarAnamnese(reqAtualizar));

        verifyNoInteractions(anamneseRepository);
    }

    @Test
    @DisplayName("Deve salvar e retornar a anamnese após atualização")
    void deveRetornarAnamnesesalvaAposAtualizacao() {
        // Arrange
        Anamnese anamneseAtualizada = new Anamnese();
        anamneseAtualizada.setAltura(reqAtualizar.altura());
        anamneseAtualizada.setPeso(reqAtualizar.peso());
        alunoComAnamnese.setAnamnese(anamneseAtualizada);

        when(jpaUserDetailsService.getCurrentAluno()).thenReturn(alunoComAnamnese);
        when(anamneseRepository.save(anamneseAtualizada)).thenReturn(anamneseAtualizada);

        // Act
        Anamnese resultado = anamneseService.atualizarAnamnese(reqAtualizar);

        // Assert
        assertNotNull(resultado);
        assertEquals(anamneseAtualizada, resultado);
    }
}
