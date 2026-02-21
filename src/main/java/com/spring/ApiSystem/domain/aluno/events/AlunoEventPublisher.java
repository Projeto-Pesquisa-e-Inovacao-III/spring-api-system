package com.spring.ApiSystem.domain.aluno.events;

import com.spring.ApiSystem.domain.aluno.Aluno;

import java.util.List;

public class AlunoEventPublisher {
    private final List<AlunosListener> listener;

    public AlunoEventPublisher(List<AlunosListener> listener) {
        this.listener = listener;
    }

    public void publishAlunoCreatedEvent(Aluno aluno) {
        listener.forEach(l -> l.onAlunoCreated(aluno));
    }

}
