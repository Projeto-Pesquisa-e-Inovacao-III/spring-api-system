package com.spring.ApiPag.validator.LengthElevenOrFourteen;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LengthElevenOrFourteenValidator implements ConstraintValidator<LengthElevenOrFourteen, String> {

    @Override
    public void initialize(LengthElevenOrFourteen constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int length = value.length();
        return length == 11 || length == 14;
    }
}
