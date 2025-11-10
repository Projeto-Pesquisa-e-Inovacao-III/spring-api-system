package com.spring.ApiSystem.horariopersonal.dto.response;

import java.time.LocalTime;

public record ResSlotDisponivelDTO(
        String inicio,
        String fim
){

    public ResSlotDisponivelDTO(LocalTime inicio, LocalTime fim) {
        this(inicio.toString(), fim.toString());
    }
}
