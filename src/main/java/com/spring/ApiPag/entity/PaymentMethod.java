package com.spring.ApiPag.entity;

import com.spring.ApiPag.enums.paymentMethod.PaymentMethodEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum type;


    public PaymentMethod() {
    }

    public PaymentMethod(Long id, PaymentMethodEnum type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethodEnum getType() {
        return type;
    }

    public void setType(PaymentMethodEnum type) {
        this.type = type;
    }
}
