package com.spring.ApiSystem.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PacoteContratado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date dataInicio;

    @Column(nullable = false)
    private Date dataFim;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Beneficio beneficio;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Pacote pacote;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Aluno aluno;

    public PacoteContratado() {
    }

    public PacoteContratado(Long id, Date dataInicio, Date dataFim,
                            Beneficio beneficio, Pacote pacote, Aluno aluno) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.beneficio = beneficio;
        this.pacote = pacote;
        this.aluno = aluno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Beneficio getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(Beneficio beneficio) {
        this.beneficio = beneficio;
    }

    public Pacote getPacote() {
        return pacote;
    }

    public void setPacote(Pacote pacote) {
        this.pacote = pacote;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
