package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.dto.response.ResBuscarAlunoPorIdDTO;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseJaExisteException;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseNaoEncontradaException;
import com.spring.ApiSystem.domain.anamnese.mapper.AnamneseMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.service.HtmlSanitizer;
import org.springframework.stereotype.Service;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDTO;
import com.spring.ApiSystem.domain.anamnese.dto.response.ResBuscarAnamneseDTO;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AlunoService alunoService;
    private final AnamneseMapper anamneseMapper;
    private final HtmlSanitizer htmlSanitizer;

    public AnamneseService(AnamneseRepository anamneseRepository, JpaUserDetailsService jpaUserDetailsService,
            AlunoService alunoService, AnamneseMapper anamneseMapper, HtmlSanitizer htmlSanitizer) {
        this.anamneseRepository = anamneseRepository;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.alunoService = alunoService;
        this.anamneseMapper = anamneseMapper;
        this.htmlSanitizer = htmlSanitizer;
    }

    public Anamnese cadastrarAnamnese(ReqCadastrarAnamneseDTO req) {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();

        if (aluno.getAtivoAnamnese()) {
            throw new AnamneseJaExisteException();
        }

        Anamnese anamnese = anamneseMapper.toEntityFromRequest(req);

        // Sanitizar campos de texto livre
        sanitizeAnamnese(anamnese);

        Aluno alunoAnamnese = alunoService.registrarAnamnese(aluno, anamnese);
        return alunoAnamnese.getAnamnese();
    }

    public Anamnese atualizarAnamnese(ReqAtualizarAnamneseDTO req) {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        Anamnese anamnese = aluno.getAnamnese();

        if (anamnese == null) {
            throw new AnamneseNaoEncontradaException();
        }

        anamneseMapper.updateEntityFromRequest(req, anamnese);

        // Sanitizar campos de texto livre
        sanitizeAnamnese(anamnese);

        return anamneseRepository.save(anamnese);
    }

    public ResBuscarAnamneseDTO buscarAnamnese() {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        Anamnese anamnese = aluno.getAnamnese();

        if (anamnese == null) {
            throw new AnamneseNaoEncontradaException();
        }

        return anamneseMapper.buscarAnamnese(anamnese);
    }

    public ResBuscarAnamneseDTO buscarAnamneseDoAluno(Long id) {
        Aluno aluno = alunoService.buscarPorId(id);
        Anamnese anamnese = aluno.getAnamnese();

        if (anamnese == null) {
            throw new AnamneseNaoEncontradaException();
        }

        return anamneseMapper.buscarAnamnese(anamnese);
    }

    /**
     * Sanitiza os campos de texto livre de uma anamnese.
     * Remove qualquer conteúdo HTML potencialmente perigoso.
     *
     * @param anamnese A anamnese a ser sanitizada
     */
    private void sanitizeAnamnese(Anamnese anamnese) {
        if (anamnese.getObjectivoPrincipal() != null) {
            anamnese.setObjectivoPrincipal(htmlSanitizer.sanitize(anamnese.getObjectivoPrincipal()));
        }
        if (anamnese.getRotina() != null) {
            anamnese.setRotina(htmlSanitizer.sanitizeNullable(anamnese.getRotina()));
        }
        if (anamnese.getObservacaoSaude() != null) {
            anamnese.setObservacaoSaude(htmlSanitizer.sanitizeNullable(anamnese.getObservacaoSaude()));
        }
        if (anamnese.getCondicoes() != null && !anamnese.getCondicoes().isEmpty()) {
            anamnese.getCondicoes().forEach(condicao -> {
                if (condicao.getSituacao() != null) {
                    condicao.setSituacao(htmlSanitizer.sanitizeNullable(condicao.getSituacao()));
                }
            });
        }
    }
}
