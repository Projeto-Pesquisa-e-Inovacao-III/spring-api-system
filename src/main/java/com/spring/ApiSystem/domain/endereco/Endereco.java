package com.spring.ApiSystem.domain.endereco;

import com.spring.ApiSystem.domain.cep.CEP;

import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CEP_id")
    private CEP cep;

    private boolean ativo = true;

    public Endereco() {
    }

    public Endereco(Long id, String numero,
                    String complemento, String unidade,
                    String tipo, Usuario usuario,
                    CEP cep, boolean ativo) {
        this.id = id;
        this.numero = numero;
        this.complemento = complemento;
        this.unidade = unidade;
        this.tipo = tipo;
        this.usuario = usuario;
        this.cep = cep;
        this.ativo = ativo;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
