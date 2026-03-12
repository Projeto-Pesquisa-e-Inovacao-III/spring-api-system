package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.stereotype.Service;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AlunoService alunoService;

    public AnamneseService(AnamneseRepository anamneseRepository, JpaUserDetailsService jpaUserDetailsService, AlunoService alunoService) {
        this.anamneseRepository = anamneseRepository;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.alunoService = alunoService;
    }

    public Anamnese cadastrarAnamnese(ReqCadastrarAnamneseDto req) {
        Anamnese anamnese = new Anamnese();

        DtoToEntity(anamnese, req);

        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        Aluno alunoAnamnese = alunoService.registrarAnamnese(aluno, anamnese);
        return alunoAnamnese.getAnamnese();
    }

    public void DtoToEntity(Anamnese anamnese, ReqCadastrarAnamneseDto dto) {
        anamnese.setAltura(dto.altura());
        anamnese.setPeso(dto.peso());
        anamnese.setObjectivoPrincipal(dto.objectivoPrincipal());
        anamnese.setRotina(dto.rotina());
        anamnese.setCondicoes(dto.condicoes());
        anamnese.setNivelDeAtividade(dto.nivelDeAtividade());
        anamnese.setObservacaoSaude(dto.observacaoSaude());
    }
}
