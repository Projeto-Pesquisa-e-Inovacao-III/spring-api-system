package com.spring.ApiPag.entity;

import com.spring.ApiPag.enums.recurrancePlanUnit.RecurranceUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "recurrence_plan_intervals")
public class RecurrencePlanInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecurranceUnit unit = RecurranceUnit.MONTH;

    @Positive
    @Column(nullable = false)
    private Integer length;

    public RecurrencePlanInterval() {}

    public RecurrencePlanInterval(RecurranceUnit unit, Integer length) {
        this.unit = unit;
        this.length = length;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecurranceUnit getUnit() {
        return unit;
    }

    public void setUnit(RecurranceUnit unit) {
        this.unit = unit;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
