package com.spring.ApiPag.validator.AllowedPaymentMethod;

import com.spring.ApiPag.enums.paymentMethod.PaymentMethodEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedPaymentMethodValidator implements ConstraintValidator<AllowedPaymentMethod, PaymentMethodEnum> {

    @Override
    public boolean isValid(PaymentMethodEnum value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull separadamente se necessário
        }
        return value == PaymentMethodEnum.CREDIT_CARD || value == PaymentMethodEnum.DEBIT_CARD;
    }
}
