package com.spring.ApiSystem.domain.personal.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReqAtualizarBufferDTO(@Min(value = 15, message = "O intervalo deve ser de no mínimo 15 minutos")
                                    @Max(value = 60, message = "O intervalo deve ser de no máximo 60 minutos")
                                    Integer bufferMinutos)
{}
