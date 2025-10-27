package com.spring.ApiPag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "dimesions_object")
public class DimesionsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Range(min = 15, max = 100)
    private Integer length;

    @NotNull
    @Range(min = 10, max = 100)
    private Integer width;

    @NotNull
    @Range(min = 1, max = 100)
    private Integer height;

    public DimesionsObject() {
    }

    public DimesionsObject(Long id, Integer length, Integer width, Integer height) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getLength() {
        return length;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}