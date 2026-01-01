package com.spring.ApiSystem.nocodetool;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class NoCode {
    @Id
    private UUID id;

    // name to identify what is being edited
    private String identifier;

    // the text that the user wrote
    private String content;

    // h1, h2, p, span, img, button etc
    private String elementTag;

    public NoCode(UUID id, String identifier, String content, String elementTag) {
        this.id = id;
        this.identifier = identifier;
        this.content = content;
        this.elementTag = elementTag;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getElementTag() {
        return elementTag;
    }

    public void setElementTag(String elementTag) {
        this.elementTag = elementTag;
    }
}
