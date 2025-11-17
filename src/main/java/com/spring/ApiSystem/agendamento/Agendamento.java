package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.agendamento.state.*;
import com.spring.ApiSystem.aluno.Aluno;
import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.personal.Personal;
import com.spring.ApiSystem.produtocontratado.ProdutoContratado;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "status")
    private AgendamentoStatus status;

    @Column(length = 200)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id", nullable = false)
    private Personal personal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_contratado_id", nullable = false)
    private ProdutoContratado produtoContratado;

    @Transient
    private AgendamentoState agendamentoState;

    public Agendamento() {
        this.agendamentoState = new AgendamentoAprovado();
        this.status = this.agendamentoState.getSituacao();
    }
    public Agendamento(Long id, LocalDateTime data, String descricao,
                       Endereco endereco, Aluno aluno, Personal personal,
                       ProdutoContratado produtoContratado) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.endereco = endereco;
        this.aluno = aluno;
        this.personal = personal;
        this.produtoContratado = produtoContratado;
        this.agendamentoState = new AgendamentoAprovado();
        this.status = this.agendamentoState.getSituacao();
    }

    @PostLoad
    private void carregarDoBanco() {
        switch (this.status) {
            case APROVADO -> this.agendamentoState = new AgendamentoAprovado();
            case PENDENTE_CLIENTE_APROVACAO -> this.agendamentoState = new AgendamentoPendenteClienteAprovacao();
            case CONCLUIDO -> this.agendamentoState = new AgendamentoConcluido();
            case PENDENTE_PERSONAL_CONCLUIR -> this.agendamentoState = new AgendamentoPendentePersonalConcluir();
            case CANCELADO_CLIENTE -> this.agendamentoState = new AgendamentoCanceladoCliente();
            case CANCELADO_PERSONAL -> this.agendamentoState = new AgendamentoCanceladoPersonal();
            case AUSENCIA_CLIENTE -> this.agendamentoState = new AgendamentoAusenciaCliente();
            case AUSENCIA_PERSONAL -> this.agendamentoState = new AgendamentoAusenciaPersonal();
            default -> this.agendamentoState = new AgendamentoPendentePersonalAprovacao();
        }
    }


    private void atualizarEstado(AgendamentoState novoEstado) {
        this.agendamentoState = novoEstado;
        this.status = novoEstado.getSituacao();
    }

    public void aprovado() { atualizarEstado(agendamentoState.aprovado()); }
    public void pendentePersonalAprovacao() { atualizarEstado(agendamentoState.pendentePersonalAprovacao()); }
    public void pendenteClienteAprovacao() { atualizarEstado(agendamentoState.pendenteClienteAprovacao()); }
    public void concluido() { atualizarEstado(agendamentoState.concluido()); }
    public void pendentePersonalConcluir() { atualizarEstado(agendamentoState.pendentePersonalConcluir()); }
    public void canceladoCliente() { atualizarEstado(agendamentoState.canceladoCliente()); }
    public void canceladoPersonal() { atualizarEstado(agendamentoState.canceladoPersonal()); }
    public void ausenciaCliente() { atualizarEstado(agendamentoState.ausenciaCliente()); }
    public void ausenciaPersonal() { atualizarEstado(agendamentoState.ausenciaPersonal()); }



    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDateTime getData() {return data;}
    public void setData(LocalDateTime data) {this.data = data;}

    public LocalDateTime getDataFim() {return dataFim;}

    public void setDataFim(LocalDateTime dataFim) {this.dataFim = dataFim;}

    public AgendamentoStatus getStatus() {return status;}
    public void setStatus(AgendamentoStatus agendamentoStatus) {this.status = agendamentoStatus;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public Endereco getEndereco() {return endereco;}
    public void setEndereco(Endereco endereco) {this.endereco = endereco;}

    public Aluno getAluno() {return aluno;}
    public void setAluno(Aluno aluno) {this.aluno = aluno;}

    public Personal getPersonal() {return personal;}
    public void setPersonal(Personal personal) {this.personal = personal;}

    public ProdutoContratado getProdutoContratado() {return produtoContratado;}
    public void setProdutoContratado(ProdutoContratado produtoContratado) {this.produtoContratado = produtoContratado;}

    public AgendamentoState getAgendamentoState() {return agendamentoState;}

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", data=" + data +
                ", situacao=" + status +
                ", descricao='" + descricao + '\'' +
                ", endereco=" + endereco +
                ", aluno=" + aluno +
                ", personal=" + personal +
                ", produtoContratado=" + produtoContratado +
                ", agendamentoState=" + agendamentoState +
                '}';
    }
}
