package com.spring.ApiSystem.produtoexibicao;

import com.spring.ApiSystem.produtoexibicao.enums.ProdutoExibicaoStatus;
import com.spring.ApiSystem.produtoexibicao.enums.TipoAula;
import com.spring.ApiSystem.produtoexibicao.enums.TipoProduto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "produto_exibicao")
public class ProdutoExibicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private String periodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "status")
    private ProdutoExibicaoStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_produto", nullable = false)
    private TipoProduto tipoProduto;

    @Column(name = "data_criacao",
            nullable = false,
            updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "tipo_aula",
            nullable = false)

    @Enumerated(EnumType.STRING)
    private TipoAula tipoAula;

    @Column(name = "quantidade_aula",
            nullable = false)
    private Integer quantidadeAula;

    @Column(name = "duracao_mes",
            nullable = false)
    private Integer duracaoMes;

    public ProdutoExibicao() {
    }

    public ProdutoExibicao(Long id, String titulo, String subtitulo, String descricao, Double preco, String periodo, ProdutoExibicaoStatus status, TipoProduto tipoProduto, LocalDateTime dataCriacao, TipoAula tipoAula, Integer quantidadeAula, Integer duracaoMes) {
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.descricao = descricao;
        this.preco = preco;
        this.periodo = periodo;
        this.status = status;
        this.tipoProduto = tipoProduto;
        this.dataCriacao = dataCriacao;
        this.tipoAula = tipoAula;
        this.quantidadeAula = quantidadeAula;
        this.duracaoMes = duracaoMes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public ProdutoExibicaoStatus getStatus() {
        return status;
    }

    public void setStatus(ProdutoExibicaoStatus status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public TipoAula getTipoAula() {return tipoAula;}

    public void setTipoAula(TipoAula tipoAula) {this.tipoAula = tipoAula;}

    public Integer getQuantidadeAula() {return quantidadeAula;}

    public void setQuantidadeAula(Integer quantidadeAula) {this.quantidadeAula = quantidadeAula;}

    public Integer getDuracaoMes() {
        return duracaoMes;
    }

    public void setDuracaoMes(Integer duracaoMes) {
        this.duracaoMes = duracaoMes;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }
}
