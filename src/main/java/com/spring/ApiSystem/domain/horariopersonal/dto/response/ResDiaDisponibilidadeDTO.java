package com.spring.ApiSystem.domain.horariopersonal.dto.response;

import com.spring.ApiSystem.domain.horariopersonal.enums.DiaSemana;

import java.util.List;

public record ResDiaDisponibilidadeDTO(
        DiaSemana dia,
        List<ResSlotDisponivelDTO> slotsDisponiveis
) {}