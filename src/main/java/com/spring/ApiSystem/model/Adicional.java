package com.spring.ApiSystem.model;

import jakarta.persistence.*;

@Entity
public class Adicional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String subtitulo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double preco;

    @JoinColumn(nullable = false)
    @ManyToOne
    private CategoriaAgendamento categoriaAgendamento;

    public Adicional() {
    }

    public Adicional(Long id, String titulo, String subtitulo,
                     Integer quantidade, Double preco,
                     CategoriaAgendamento categoriaAgendamento) {
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.categoriaAgendamento = categoriaAgendamento;
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public CategoriaAgendamento getCategoriaAgendamento() {
        return categoriaAgendamento;
    }

    public void setCategoriaAgendamento(CategoriaAgendamento categoriaAgendamento) {
        this.categoriaAgendamento = categoriaAgendamento;
    }
}
