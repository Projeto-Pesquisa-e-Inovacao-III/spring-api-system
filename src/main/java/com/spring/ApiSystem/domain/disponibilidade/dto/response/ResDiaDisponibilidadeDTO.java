package com.spring.ApiSystem.domain.disponibilidade.dto.response;

import com.spring.ApiSystem.domain.disponibilidade.enums.DiaSemana;

import java.util.List;

public record ResDiaDisponibilidadeDTO(
        DiaSemana dia,
        List<ResSlotDisponivelDTO> slotsDisponiveis
) {}