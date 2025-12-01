package com.spring.ApiSystem.aluno.dto.response;

public class ResAlunosPagantesDTO {
    private Integer quantidadeAlunos;

    public ResAlunosPagantesDTO() {}

    public ResAlunosPagantesDTO(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
}

