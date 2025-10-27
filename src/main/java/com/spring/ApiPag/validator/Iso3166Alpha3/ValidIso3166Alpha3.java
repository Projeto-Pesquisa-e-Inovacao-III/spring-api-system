package com.spring.ApiPag.validator.Iso3166Alpha3;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Iso3166Alpha3Validator.class)
@Documented
public @interface ValidIso3166Alpha3 {
    String message() default "Invalid ISO 3166-1 alpha-3 code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}