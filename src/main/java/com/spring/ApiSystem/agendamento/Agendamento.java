package com.spring.ApiSystem.agendamento;

import com.spring.ApiSystem.agendamento.enums.Situacao;
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
    private Situacao situacao;

    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
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
    }

    public Agendamento(Long id, LocalDateTime data, String descricao,
                       Endereco endereco, Aluno aluno, Personal personal,
                       ProdutoContratado produtoContratado, AgendamentoState state) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.endereco = endereco;
        this.aluno = aluno;
        this.personal = personal;
        this.produtoContratado = produtoContratado;

        definirEstadoInicial(state);
    }

    @PostLoad
    private void carregarDoBanco() {
        definirEstadoInicial(null);
    }


    private void definirEstadoInicial(AgendamentoState state) {
        if (state != null) {
            this.agendamentoState = state;
        } else {
            switch (this.situacao) {
                case ACEITO -> this.agendamentoState = new AgendamentoAceito();
                case PENDENTE_CLIENTE -> this.agendamentoState = new AgendamentoPendenteCliente();
                case RECUSADO -> this.agendamentoState = new AgendamentoRecusado();
                case CONCLUIDO -> this.agendamentoState = new AgendamentoConcluido();
                default -> this.agendamentoState = new AgendamentoPendentePersonal();
            }
        }

        this.situacao = this.agendamentoState.getSituacao();
    }

    private void atualizarEstado(AgendamentoState novoEstado) {
        this.agendamentoState = novoEstado;
        this.situacao = novoEstado.getSituacao();
    }

    public void aceitar() { atualizarEstado(agendamentoState.aceitar()); }
    public void recusado() { atualizarEstado(agendamentoState.recusado()); }
    public void concluido() { atualizarEstado(agendamentoState.concluido()); }
    public void pendentePersonal() { atualizarEstado(agendamentoState.pendentePersonal()); }
    public void pendenteCliente() { atualizarEstado(agendamentoState.pendenteCliente()); }



    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDateTime getData() {return data;}
    public void setData(LocalDateTime data) {this.data = data;}

    public Situacao getSituacao() {return situacao;}
    public void setSituacao(Situacao situacao) {this.situacao = situacao;}

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
                ", situacao=" + situacao +
                ", descricao='" + descricao + '\'' +
                ", endereco=" + endereco +
                ", aluno=" + aluno +
                ", personal=" + personal +
                ", produtoContratado=" + produtoContratado +
                ", agendamentoState=" + agendamentoState +
                '}';
    }
}
