package com.conexa.backend.scheduling.domain.exceptions.auth;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class PasswordsDoNotMatchException extends ExceptionMessageBuilder {
    public PasswordsDoNotMatchException() {
        super(
                "Password Mismatch",
                "Your passwords do not match. Please check your input and try again.",
                400
        );
    }
}
