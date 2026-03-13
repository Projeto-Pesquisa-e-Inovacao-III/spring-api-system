package com.spring.ApiSystem.domain.anamnese;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseJaExisteException;
import com.spring.ApiSystem.domain.anamnese.exception.AnamneseNaoEncontradaException;
import com.spring.ApiSystem.domain.anamnese.mapper.AnamneseMapper;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.stereotype.Service;

import com.spring.ApiSystem.domain.anamnese.dto.request.ReqAtualizarAnamneseDto;
import com.spring.ApiSystem.domain.anamnese.dto.request.ReqCadastrarAnamneseDto;

@Service
public class AnamneseService {
    private final AnamneseRepository anamneseRepository;
    private final JpaUserDetailsService jpaUserDetailsService;
    private final AlunoService alunoService;
    private final AnamneseMapper anamneseMapper;

    public AnamneseService(AnamneseRepository anamneseRepository, JpaUserDetailsService jpaUserDetailsService, AlunoService alunoService, AnamneseMapper anamneseMapper) {
        this.anamneseRepository = anamneseRepository;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.alunoService = alunoService;
        this.anamneseMapper = anamneseMapper;
    }

    public Anamnese cadastrarAnamnese(ReqCadastrarAnamneseDto req) {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        
        if(aluno.getAtivoAnamnese()){
            throw new AnamneseJaExisteException();
        }

        Anamnese anamnese = anamneseMapper.toEntityFromRequest(req);


        Aluno alunoAnamnese = alunoService.registrarAnamnese(aluno, anamnese);
        return alunoAnamnese.getAnamnese();
    }

    public Anamnese atualizarAnamnese(ReqAtualizarAnamneseDto req) {
        Aluno aluno = jpaUserDetailsService.getCurrentAluno();
        Anamnese anamnese = aluno.getAnamnese();

        if(anamnese == null){
            throw new AnamneseNaoEncontradaException();
        }

        anamneseMapper.updateEntityFromRequest(req, anamnese);

        return anamneseRepository.save(anamnese);
    }
}
