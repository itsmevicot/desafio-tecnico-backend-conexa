package com.conexa.backend.scheduling.presentation.api.v1.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SecurePasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurePassword {
    String message() default "Password must be between 8 and 16 characters long, and include at least one number and one special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

