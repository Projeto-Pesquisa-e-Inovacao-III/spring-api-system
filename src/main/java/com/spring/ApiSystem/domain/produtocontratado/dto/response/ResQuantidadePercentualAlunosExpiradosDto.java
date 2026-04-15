package com.spring.ApiSystem.domain.produtocontratado.dto.response;

public class ResQuantidadePercentualAlunosExpiradosDto {
    private Integer quantidadeAlunos;
    private Double percentualAlunos;

    public ResQuantidadePercentualAlunosExpiradosDto() {}

    public ResQuantidadePercentualAlunosExpiradosDto(Integer quantidadeAlunos, Double percentualAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
        this.percentualAlunos = percentualAlunos;
    }

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    public Double getPercentualAlunos() {
        return percentualAlunos;
    }

    public void setPercentualAlunos(Double percentualAlunos) {
        this.percentualAlunos = percentualAlunos;
    }
}

