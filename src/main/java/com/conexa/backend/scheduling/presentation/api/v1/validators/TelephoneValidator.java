package com.conexa.backend.scheduling.presentation.api.v1.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelephoneValidator implements ConstraintValidator<ValidTelephone, String> {

    private static final String TELEPHONE_PATTERN = "^\\d{10,15}$";

    @Override
    public boolean isValid(String telephone, ConstraintValidatorContext context) {
        if (telephone == null || telephone.isEmpty()) {
            return false;
        }
        return telephone.matches(TELEPHONE_PATTERN);
    }
}

