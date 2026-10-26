package com.spring.ApiSystem.domain.recomendacaotreino;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "recomendacao_treino")
public class RecomendacaoTreino {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @Lob
    private String treino;

    public RecomendacaoTreino() {
    }

    public RecomendacaoTreino(Long id, Agendamento agendamento, LocalDate dataCriacao, String treino) {
        this.id = id;
        this.agendamento = agendamento;
        this.dataCriacao = dataCriacao;
        this.treino = treino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTreino() {
        return treino;
    }

    public void setTreino(String treino) {
        this.treino = treino;
    }
}
