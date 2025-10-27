package com.spring.ApiPag.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @Length(min = 1, max = 100)
    @Column(unique = true)
    private String reference_id;

    @Length(min = 1, max = 100)
    private String name;

    @Length(min = 1, max = 255)
    private String description;

    @NotNull
    @Max(999)
    @Positive
    private Integer quantity;

    @NotNull
    @Max(999999900)
    @Positive
    private Integer unit_amount;

    @URL(protocol = "http")
    private String image_url;

    public Item() {
    }

    public Item(Long id, String reference_id, String name, String description, Integer quantity, Integer unit_amount, String image_url) {
        this.id = id;
        this.reference_id = reference_id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit_amount = unit_amount;
        this.image_url = image_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnit_amount() {
        return unit_amount;
    }

    public void setUnit_amount(Integer unit_amount) {
        this.unit_amount = unit_amount;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
