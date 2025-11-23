package com.spring.ApiSystem.endereco;

import com.spring.ApiSystem.cep.CEP;
import com.spring.ApiSystem.usuario.Usuario;
import jakarta.persistence.*;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;

@Entity(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    private String complemento;

    @Column(nullable = true)
    private String unidade;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "data_criacao",
            nullable = false,
            updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CEP_id")
    private CEP cep;

    public Endereco() {
    }

    public Endereco(Long id, String numero,
                    String complemento, String unidade,
                    String tipo, LocalDateTime dataCriacao,
                    LocalDateTime dataAtualizacao,
                    Usuario usuario, CEP cep) {
        this.id = id;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.tipo = tipo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.usuario = usuario;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public CEP getCep() {
        return cep;
    }

    public void setCep(CEP cep) {
        this.cep = cep;
    }
}
