package com.spring.ApiPag.entity;

import com.spring.ApiPag.enums.paymentMethod.PaymentMethodEnum;
import com.spring.ApiPag.validator.AllowedPaymentMethod.AllowedPaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "payment_methods_configs")
public class PaymentMethodsConfigs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodConfig_id;

    @NotNull
    @AllowedPaymentMethod
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethodEnum type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodConfig_id")
    private List<ConfigOption> configs;

    public PaymentMethodsConfigs() {}

    public PaymentMethodsConfigs(PaymentMethodEnum type, List<ConfigOption> configs) {
        this.type = type;
        this.configs = configs;
    }

    // Getters and Setters
    public Long getPaymentMethodConfig_id() {
        return paymentMethodConfig_id;
    }

    public void setPaymentMethodConfig_id(Long paymentMethodConfig_id) {
        this.paymentMethodConfig_id = paymentMethodConfig_id;
    }

    public PaymentMethodEnum getType() {
        return type;
    }

    public void setType(PaymentMethodEnum type) {
        this.type = type;
    }

    public List<ConfigOption> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigOption> configs) {
        this.configs = configs;
    }
}
