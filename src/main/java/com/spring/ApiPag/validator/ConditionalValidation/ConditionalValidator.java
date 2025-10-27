package com.spring.ApiPag.validator.ConditionalValidation;

import org.springframework.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class ConditionalValidator implements ConstraintValidator<ConditionalValidation, Object> {

    private String conditionalField;
    private String conditionalValue;
    private String requiredField;
    private String customMessage;

    @Override
    public void initialize(ConditionalValidation constraintAnnotation) {
        this.conditionalField = constraintAnnotation.conditionalField();
        this.conditionalValue = constraintAnnotation.conditionalValue();
        this.requiredField = constraintAnnotation.requiredField();
        this.customMessage = constraintAnnotation.customMessage();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object conditionalFieldValue = getFieldValue(object, conditionalField);
            Object requiredFieldValue = getFieldValue(object, requiredField);

            if (conditionalValue.equals(String.valueOf(conditionalFieldValue))) {
                if (requiredFieldValue == null ||
                        (requiredFieldValue instanceof String && !StringUtils.hasText((String) requiredFieldValue))) {

                    String message = StringUtils.hasText(customMessage)
                            ? customMessage
                            : String.format("%s is required when %s is '%s'",
                            requiredField, conditionalField, conditionalValue);

                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(requiredField)
                            .addConstraintViolation();

                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }
}
