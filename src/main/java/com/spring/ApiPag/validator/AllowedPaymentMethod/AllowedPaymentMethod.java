package com.spring.ApiPag.validator.AllowedPaymentMethod;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedPaymentMethodValidator.class)
public @interface AllowedPaymentMethod {
    String message() default "Método de pagamento deve ser CREDIT_CARD ou DEBIT_CARD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
