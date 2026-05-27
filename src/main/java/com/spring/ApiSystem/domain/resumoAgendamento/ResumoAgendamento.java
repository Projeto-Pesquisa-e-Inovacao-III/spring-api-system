package com.spring.ApiSystem.domain.resumoAgendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.resumoAgendamento.enums.GrupoMuscular;
import jakarta.persistence.*;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(nullable = false, length = 200)
    private String resumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<GrupoMuscular> grupoMuscular;

    public ResumoAgendamento() {
    }

    public ResumoAgendamento(Long id, Aluno aluno, Personal personal,
                             Agendamento agendamento, String resumo,
                             List<GrupoMuscular> grupoMuscular) {
        this.id = id;
        this.aluno = aluno;
        this.personal = personal;
        this.agendamento = agendamento;
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

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public List<GrupoMuscular> getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(List<GrupoMuscular> grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }
}
