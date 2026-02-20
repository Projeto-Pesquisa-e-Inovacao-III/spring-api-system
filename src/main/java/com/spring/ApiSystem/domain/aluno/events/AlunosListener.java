package com.spring.ApiSystem.eventos.aluno;

import com.spring.ApiSystem.domain.aluno.Aluno;

public interface AlunosListener {
    void onAlunoCreated(Aluno aluno);
}
