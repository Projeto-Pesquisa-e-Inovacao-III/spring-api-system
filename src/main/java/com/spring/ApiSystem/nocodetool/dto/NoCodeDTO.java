package com.spring.ApiSystem.nocodetool.dto;

import com.spring.ApiSystem.nocodetool.NoCode;

import java.util.UUID;

public class NoCodeDTO {
    private UUID id;
    private String identifier;
    private String content;
    private String elementTag;


    public NoCodeDTO() {
    }

    public NoCodeDTO(UUID id, String identifier, String content, String elementTag) {
        this.id = id;
        this.identifier = identifier;
        this.content = content;
        this.elementTag = elementTag;
    }

    public NoCodeDTO(NoCode noCode) {
        this.identifier = noCode.getIdentifier();
        this.content = noCode.getContent();
        this.elementTag = noCode.getElementTag();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
