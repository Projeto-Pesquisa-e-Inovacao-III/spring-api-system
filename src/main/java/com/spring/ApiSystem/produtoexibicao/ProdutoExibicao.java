package com.spring.ApiSystem.produtoexibicao;

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

    @Column(nullable = false)
    private String status;

    @Column(name = "data_criacao",
            nullable = false,
            updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "tipo_aula",
            nullable = false)
    private String tipoAula;

    @Column(name = "quantidade_aula",
            nullable = false)
    private Integer quantidadeAula;

    @Column(name = "duracaoMes",
            nullable = false)
    private Integer duracaoMes;

    public ProdutoExibicao() {
    }

    public ProdutoExibicao(Long id, String titulo,
                           String subtitulo, String descricao,
                           Double preco, String periodo,
                           String status, LocalDateTime dataCriacao,
                           LocalDateTime dataAtualizacao, String tipoAula,
                           Integer quantidadeAula, Integer duracaoMes) {
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.descricao = descricao;
        this.preco = preco;
        this.periodo = periodo;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }

    public Integer getQuantidadeAula() {
        return quantidadeAula;
    }

    public void setQuantidadeAula(Integer quantidadeAula) {
        this.quantidadeAula = quantidadeAula;
    }

    public Integer getDuracaoMes() {
        return duracaoMes;
    }

    public void setDuracaoMes(Integer duracaoMes) {
        this.duracaoMes = duracaoMes;
    }
}
