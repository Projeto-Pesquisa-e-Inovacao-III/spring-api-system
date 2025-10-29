package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.exception.AlunoNaoExisteExcpetion;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public BuscarAlunoPorIdDTO buscarAlunoPorId(Long id) {
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

    public Aluno findById(Long id) {
        return alunoRepository
                .findById(id)
                .orElseThrow(AlunoNaoExisteExcpetion::new);
    }

}
