package com.spring.ApiSystem.horariopersonal.dto.response;

import com.spring.ApiSystem.enums.DiaSemana;

import java.util.List;

public record ResDiaDisponibilidadeDTO(
        DiaSemana dia,
        List<ResSlotDisponivelDTO> slotsDisponiveis
) {}