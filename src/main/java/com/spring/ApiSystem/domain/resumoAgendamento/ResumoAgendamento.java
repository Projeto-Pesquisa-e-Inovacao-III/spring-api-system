package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import jakarta.persistence.*;

@Entity(name = "resumo_agendamento")
public class ResumoAgendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    private Personal personal;

    @Column(nullable = false, length = 200)
    private String resumo;

    @Enumerated(EnumType.STRING)
    private GrupoMuscular grupoMuscular;

    public ResumoAgendamento() {
    }

    public ResumoAgendamento(Long id, Aluno aluno, Personal personal, String resumo, GrupoMuscular grupoMuscular) {
        this.id = id;
        this.aluno = aluno;
        this.personal = personal;
        this.resumo = resumo;
        this.grupoMuscular = grupoMuscular;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public GrupoMuscular getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(GrupoMuscular grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }
}
