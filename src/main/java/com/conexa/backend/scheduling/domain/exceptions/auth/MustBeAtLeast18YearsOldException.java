package com.conexa.backend.scheduling.domain.exceptions.auth;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class MustBeAtLeast18YearsOldException extends ExceptionMessageBuilder {
    public MustBeAtLeast18YearsOldException() {
        super(
                "Age Restriction",
                "You must be at least 18 years old to register",
                400
        );
    }
}
