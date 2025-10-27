package com.spring.ApiPag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "recurrence_plans")
public class RecurrencePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Positive
    @Column(nullable = false)
    private Integer billing_cycle;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "interval_id")
    private RecurrencePlanInterval interval;

    public RecurrencePlan() {}

    public RecurrencePlan(String name, Integer billing_cycle, RecurrencePlanInterval interval) {
        this.name = name;
        this.billing_cycle = billing_cycle;
        this.interval = interval;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBilling_cycle() {
        return billing_cycle;
    }

    public void setBilling_cycle(Integer billing_cycle) {
        this.billing_cycle = billing_cycle;
    }

    public RecurrencePlanInterval getInterval() {
        return interval;
    }

    public void setInterval(RecurrencePlanInterval interval) {
        this.interval = interval;
    }
}
