package com.spring.ApiPag.validator.Iso3166Alpha3;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Locale;
import java.util.Set;

public class Iso3166Alpha3Validator implements ConstraintValidator<ValidIso3166Alpha3, String> {

    private static final Set<String> VALID_CODES = Set.copyOf(
            Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3)
    );

    @Override
    public void initialize(ValidIso3166Alpha3 constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotNull handle null validation if needed
        }

        return VALID_CODES.contains(value.toUpperCase());
    }
}
