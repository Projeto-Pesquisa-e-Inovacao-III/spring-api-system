package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.aluno.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.exception.AlunoNaoExisteExcpetion;
import com.spring.ApiSystem.model.Aluno;
import com.spring.ApiSystem.repository.AlunoRepository;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public BuscarAlunoPorIdDTO buscarAlunoPorId(Integer id) {
        Aluno aluno = findById(id);

        return new BuscarAlunoPorIdDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getSexo(),
                aluno.getDataNascimento(),
                aluno.getEmail(),
                aluno.getCpf(),
                aluno.isAtivo()
        );
    }

    public Aluno findById(Integer id) {
        return alunoRepository
                .findById(id)
                .orElseThrow(AlunoNaoExisteExcpetion::new);
    }

}
