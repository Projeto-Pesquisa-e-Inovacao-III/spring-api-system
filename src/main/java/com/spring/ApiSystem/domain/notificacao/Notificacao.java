package com.spring.ApiSystem.domain.notificacao;

import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "notificacao",
        indexes = {
                @Index(name = "idx_notificacao_usuario_created", columnList = "usuario_id, created_at DESC"),
                @Index(name = "idx_notificacao_usuario_lida", columnList = "usuario_id, lida")
        }
)
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "mensagem", nullable = false, length = 1000)
    private String mensagem;

    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "dados_adicionais", length = 2000)
    private String dadosAdicionais;

    @Column(name = "lida", nullable = false)
    private boolean lida = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "read_at")
    private Instant readAt;

    public Notificacao() {
    }

    public Notificacao(Usuario usuario, String titulo, String mensagem, String tipo) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.lida = false;
    }

    public Notificacao(Usuario usuario, String titulo, String mensagem, String tipo, String dadosAdicionais) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.dadosAdicionais = dadosAdicionais;
        this.lida = false;
    }

    @PrePersist
    void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
    }

    public void marcarComoLida() {
        this.lida = true;
        this.readAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDadosAdicionais() {
        return dadosAdicionais;
    }

    public void setDadosAdicionais(String dadosAdicionais) {
        this.dadosAdicionais = dadosAdicionais;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }
}
