package com.conexa.backend.scheduling.domain.exceptions.auth;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class InvalidCredentialsException extends ExceptionMessageBuilder {
    public InvalidCredentialsException() {
        super(
                "Invalid Credentials",
                "The provided credentials are invalid.",
                401
        );
    }
}
