package com.conexa.backend.scheduling.domain.exceptions.auth;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class InvalidCPFException extends ExceptionMessageBuilder {
    public InvalidCPFException() {
        super(
                "Invalid CPF",
                "The informed CPF is invalid",
                400
        );
    }
}
