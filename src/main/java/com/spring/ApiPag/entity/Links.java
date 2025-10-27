package com.spring.ApiPag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Links {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "rel",nullable = false)
    private String rel;

    @NotBlank
    @Column(name = "href",nullable = false)
    private String href;

    @NotBlank
    @Column(name = "method",nullable = false)
    private String method;

    public Links() {
    }

    public Links(Long id, String rel, String href, String method) {
        this.id = id;
        this.rel = rel;
        this.href = href;
        this.method = method;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
