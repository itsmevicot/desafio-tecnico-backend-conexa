package com.conexa.backend.scheduling.domain.exceptions.doctor;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class DoctorAlreadyExistsException extends ExceptionMessageBuilder {
    public DoctorAlreadyExistsException(String message) {
        super(
                "Doctor Already Exists",
                message,
                409
        );
    }
}
