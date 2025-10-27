package com.spring.ApiPag.validator.ConditionalValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConditionalValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConditionalValidations.class) // Allows multiple annotations on same class
public @interface ConditionalValidation {
    String message() default "Field is required when condition is met";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String conditionalField();
    String conditionalValue();
    String requiredField();
    String customMessage() default "";
}
