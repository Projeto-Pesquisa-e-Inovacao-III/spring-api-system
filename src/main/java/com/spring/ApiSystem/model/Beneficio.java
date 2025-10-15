package com.spring.ApiSystem.model;

import jakarta.persistence.*;

@Entity
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer qtdAgendamentos;

    public Beneficio() {
    }

    public Beneficio(Long id, Integer qtdAgendamentos) {
        this.id = id;
        this.qtdAgendamentos = qtdAgendamentos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQtdAgendamentos() {
        return qtdAgendamentos;
    }

    public void setQtdAgendamentos(Integer qtdAgendamentos) {
        this.qtdAgendamentos = qtdAgendamentos;
    }
}
