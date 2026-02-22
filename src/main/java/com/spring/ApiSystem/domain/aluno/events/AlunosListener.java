package com.spring.ApiSystem.domain.aluno.events;

import com.spring.ApiSystem.domain.aluno.Aluno;

public interface AlunosListener {
    void onAlunoCreated(Aluno aluno);
}
