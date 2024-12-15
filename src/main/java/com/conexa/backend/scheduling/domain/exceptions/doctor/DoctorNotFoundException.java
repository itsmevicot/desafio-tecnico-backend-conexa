package com.conexa.backend.scheduling.domain.exceptions.doctor;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;


public class DoctorNotFoundException extends ExceptionMessageBuilder {
    public DoctorNotFoundException(String message) {
        super(
                "Doctor Not Found",
                message,
                404
        );
    }
}
