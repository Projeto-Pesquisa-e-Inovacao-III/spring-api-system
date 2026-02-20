package com.spring.ApiSystem.historicoagendamento;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.endereco.Endereco;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "historico_agendamento")
public class HistoricoAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    private TipoAula tipoAula;

    @Column(name = "motivo", nullable = false, length = 200)
    private String motivo;

    @Enumerated(EnumType.STRING)
    private AgendamentoStatus status;

    @Column(name = "data_criacao", nullable = true)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    public HistoricoAgendamento() {}

    public HistoricoAgendamento(Long id, LocalDateTime dataHora, TipoAula tipoAula, String motivo, AgendamentoStatus status, LocalDateTime dataCriacao, Agendamento agendamento, Usuario usuario, Endereco endereco) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipoAula = tipoAula;
        this.motivo = motivo;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.agendamento = agendamento;
        this.usuario = usuario;
        this.endereco = endereco;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public LocalDateTime getDataHora() {return dataHora;}

    public void setDataHora(LocalDateTime dataHora) {this.dataHora = dataHora;}

    public LocalDateTime getDataFim() {return dataFim;}

    public void setDataFim(LocalDateTime dataFim) {this.dataFim = dataFim;}

    public TipoAula getTipoAula() {return tipoAula;}

    public void setTipoAula(TipoAula tipoAula) {this.tipoAula = tipoAula;}

    public String getMotivo() {return motivo;}

    public void setMotivo(String motivo) {this.motivo = motivo;}

    public AgendamentoStatus getStatus() {return status;}

    public void setStatus(AgendamentoStatus status) {this.status = status;}

    public LocalDateTime getDataCriacao() {return dataCriacao;}

    public void setDataCriacao(LocalDateTime dataCriacao) {this.dataCriacao = dataCriacao;}

    public Agendamento getAgendamento() {return agendamento;}

    public void setAgendamento(Agendamento agendamento) {this.agendamento = agendamento;}

    public Usuario getUsuario() {return usuario;}

    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    public Endereco getEndereco() {return endereco;}

    public void setEndereco(Endereco endereco) {this.endereco = endereco;}
}
