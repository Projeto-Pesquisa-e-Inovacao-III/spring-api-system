package com.spring.ApiPag.entity;

import com.spring.ApiPag.enums.configOption.ConfigOptionEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "config_options")
public class ConfigOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long configOption_id;

    @NotNull(message = "Opção é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(name = "config_option", nullable = false)
    private ConfigOptionEnum option;

    @NotBlank(message = "Valor é obrigatório")
    @Column(nullable = false)
    private String value;

    public ConfigOption() {}

    public ConfigOption(Long configOption_id, ConfigOptionEnum option, String value) {
        this.configOption_id = configOption_id;
        this.option = option;
        this.value = value;
    }

    // Getters and Setters
    public Long getConfigOption_id() {
        return configOption_id;
    }

    public void setConfigOption_id(Long configOption_id) {
        this.configOption_id = configOption_id;
    }

    public ConfigOptionEnum getOption() {
        return option;
    }

    public void setOption(ConfigOptionEnum option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
