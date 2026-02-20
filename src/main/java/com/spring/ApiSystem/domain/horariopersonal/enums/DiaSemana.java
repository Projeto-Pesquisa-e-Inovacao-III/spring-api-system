package com.spring.ApiSystem.enums;

import com.spring.ApiSystem.domain.horariopersonal.exception.DiaSemanaNaoExisteExcption;

import java.time.DayOfWeek;

public enum DiaSemana {
    SEGUNDA("segunda", DayOfWeek.MONDAY),
    TERCA("terca", DayOfWeek.TUESDAY),
    QUARTA("quarta", DayOfWeek.WEDNESDAY),
    QUINTA("quinta", DayOfWeek.THURSDAY),
    SEXTA("sexta", DayOfWeek.FRIDAY),
    SABADO("sabado", DayOfWeek.SATURDAY),
    DOMINGO("domingo", DayOfWeek.SUNDAY);

    private final String valorBD;
    private final DayOfWeek dayOfWeek;

    DiaSemana(String valorBD, DayOfWeek dayOfWeek) {
        this.valorBD = valorBD;
        this.dayOfWeek = dayOfWeek;
    }

    public String getValorBD() {
        return valorBD;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public static DiaSemana fromDayOfWeek(DayOfWeek dayOfWeek) {
        for (DiaSemana d : DiaSemana.values()) {
            if (d.dayOfWeek == dayOfWeek) {
                return d;
            }
        }
        throw new DiaSemanaNaoExisteExcption();
    }

}
