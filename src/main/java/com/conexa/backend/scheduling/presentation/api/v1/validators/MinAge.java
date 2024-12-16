package com.conexa.backend.scheduling.presentation.api.v1.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {

    String message() default "You must be at least 18 years old.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value();
}
