package com.spring.ApiSystem.domain.horariopersonal.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record ResSlotDisponivelDTO(
        @JsonFormat(pattern = "HH:mm")
        String inicio,
        @JsonFormat (pattern = "HH:mm")
        String fim
){

    public ResSlotDisponivelDTO(LocalTime inicio, LocalTime fim) {
        this(inicio.toString(), fim.toString());
    }
}
