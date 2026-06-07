package com.spring.ApiSystem.domain.nocodetool;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "no_code_image")
public class NoCodeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "no_code_id", nullable = false)
    private NoCode noCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public NoCodeImage() {
    }

    public NoCodeImage(String url, String section, NoCode noCode) {
        this.url = url;
        this.section = section;
        this.noCode = noCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public NoCode getNoCode() {
        return noCode;
    }

    public void setNoCode(NoCode noCode) {
        this.noCode = noCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
