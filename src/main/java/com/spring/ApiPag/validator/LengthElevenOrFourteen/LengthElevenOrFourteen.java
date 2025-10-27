package com.spring.ApiPag.validator.LengthElevenOrFourteen;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LengthElevenOrFourteenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LengthElevenOrFourteen {

    String message() default "Length must be either 11 or 14 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
