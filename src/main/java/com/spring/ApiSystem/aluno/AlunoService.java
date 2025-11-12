package com.spring.ApiSystem.aluno;

import com.spring.ApiSystem.aluno.dto.response.BuscarAlunoPorIdDTO;
import com.spring.ApiSystem.aluno.exception.AlunoNaoExisteExcpetion;
import com.spring.ApiSystem.usuario.exception.CpfExistenteException;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Aluno cadastrarAluno(Aluno aluno) {
        if (alunoRepository.existsByCpf(aluno.getCpf())) {
            throw new CpfExistenteException();
        }
       return alunoRepository.save(aluno);
    }

    public BuscarAlunoPorIdDTO buscarAlunoPorId(Long id) {
        Aluno aluno = buscarPorId(id);

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

    public Aluno buscarPorId(Long id) {
        return alunoRepository
                .findById(id)
                .orElseThrow(AlunoNaoExisteExcpetion::new);
    }

}
