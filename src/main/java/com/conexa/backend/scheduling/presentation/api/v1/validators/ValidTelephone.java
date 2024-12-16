package com.conexa.backend.scheduling.presentation.api.v1.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TelephoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTelephone {
    String message() default "Telephone must have 10 or 11 digits.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
