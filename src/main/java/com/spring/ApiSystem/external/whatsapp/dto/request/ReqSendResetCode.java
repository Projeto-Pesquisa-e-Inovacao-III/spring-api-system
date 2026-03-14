package com.spring.ApiSystem.external.whatsapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReqSendResetCode(
        @NotBlank
        @Pattern(regexp = "\\d{1,3}", message = "pais deve conter apenas dígitos (1-3).")
        String pais,

        @NotBlank
        @Pattern(regexp = "\\d{2,3}", message = "ddd deve conter apenas dígitos (2-3).")
        String ddd,

        @NotBlank
        @Pattern(regexp = "\\d{7,11}", message = "numero deve conter apenas dígitos (7-11).")
        @Size(min = 7, max = 11)
        String numero
) {}