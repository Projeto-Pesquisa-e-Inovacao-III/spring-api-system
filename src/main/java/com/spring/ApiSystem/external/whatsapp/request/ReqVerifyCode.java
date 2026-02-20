package com.spring.ApiSystem.externalservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReqVerifyCode(
        @NotBlank
        @Pattern(regexp = "\\d{1,3}", message = "pais deve conter apenas dígitos (1-3).")
        String pais,

        @NotBlank
        @Pattern(regexp = "\\d{2,3}", message = "ddd deve conter apenas dígitos (2-3).")
        String ddd,

        @NotBlank
        @Pattern(regexp = "\\d{7,11}", message = "numero deve conter apenas dígitos (7-11).")
        String numero,

        @NotBlank
        @Pattern(regexp = "\\d{6}", message = "verificationCode deve ser um código de 6 dígitos.")
        String verificationCode
) {
}
