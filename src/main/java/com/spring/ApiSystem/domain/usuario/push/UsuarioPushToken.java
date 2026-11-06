package com.spring.ApiSystem.domain.usuario.push;

import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "usuario_push_token",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_push_token_token", columnNames = "push_token")
        }
)
public class UsuarioPushToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "push_token", nullable = false, length = 255)
    private String pushToken;

    @Column(name = "platform", nullable = false, length = 20)
    private String platform = "android";

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UsuarioPushToken() {
    }

    public UsuarioPushToken(Usuario usuario, String pushToken, String platform) {
        this.usuario = usuario;
        this.pushToken = pushToken;
        this.platform = platform;
        this.ativo = true;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
