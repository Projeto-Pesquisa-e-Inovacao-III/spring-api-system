package com.spring.ApiSystem.domain.nocodetool;

import com.spring.ApiSystem.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "no_code")
public class NoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = false)
    private Usuario user;

    @Column(nullable = false)
    private String modificationName;

    @Column(nullable = true)
    private String description;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime restoredAt;

    @Column(nullable = true)
    private UUID restoredFromId;

    public NoCode() {
    }

    public NoCode(UUID id, Usuario user, String modificationName, String description, String content,
            LocalDateTime createdAt, LocalDateTime restoredAt, UUID restoredFromId) {
        this.id = id;
        this.user = user;
        this.modificationName = modificationName;
        this.description = description;
        this.content = content;
        this.createdAt = createdAt;
        this.restoredAt = restoredAt;
        this.restoredFromId = restoredFromId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getModificationName() {
        return modificationName;
    }

    public void setModificationName(String modificationName) {
        this.modificationName = modificationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getRestoredAt() {
        return restoredAt;
    }

    public void setRestoredAt(LocalDateTime restoredAt) {
        this.restoredAt = restoredAt;
    }

    public UUID getRestoredFromId() {
        return restoredFromId;
    }

    public void setRestoredFromId(UUID restoredFromId) {
        this.restoredFromId = restoredFromId;
    }
}
