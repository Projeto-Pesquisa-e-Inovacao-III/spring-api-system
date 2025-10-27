package com.spring.ApiPag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "box")
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_id", nullable = false)
    private Long box_id;

    @NotNull
    private String weight;

    @OneToOne
    @NotNull
    private DimesionsObject dimesionsObject;

    public Box() {
    }

    public Box(Long box_id, String weight, DimesionsObject dimesionsObject) {
        this.box_id = box_id;
        this.weight = weight;
        this.dimesionsObject = dimesionsObject;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public DimesionsObject getDimesionsObject() {
        return dimesionsObject;
    }

    public void setDimesionsObject(DimesionsObject dimesionsObject) {
        this.dimesionsObject = dimesionsObject;
    }

    public Long getId() {
        return box_id;
    }

    public void setId(Long box_id) {
        this.box_id = box_id;
    }

}