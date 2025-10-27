package com.spring.ApiPag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long phone_id;

    @NotBlank
    @Pattern(regexp = "^(\\+55|55)$")
    private String country;

    @NotBlank
    @Pattern(regexp = "^[1-9]\\d$")
    private String area;

    @NotBlank
    @Pattern(regexp = "^9\\d{8}")
    private String number;

    public String getCountry() {
        return country;
    }

    public Long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(Long phone_id) {
        this.phone_id = phone_id;
    }
}
