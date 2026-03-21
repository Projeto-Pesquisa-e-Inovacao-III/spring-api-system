package com.spring.ApiSystem.domain.disponibilidade.dto.response;

import com.spring.ApiSystem.shared.enums.DiaSemana;

import java.util.List;

public record ResDiaDisponibilidadeDTO(
        DiaSemana dia,
        List<ResSlotDisponivelDTO> slotsDisponiveis
) {}