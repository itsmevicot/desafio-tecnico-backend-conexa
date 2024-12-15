package com.conexa.backend.scheduling.domain.exceptions.patient;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;


public class PatientNotFoundException extends ExceptionMessageBuilder {
    public PatientNotFoundException() {
        super(
                "Patient Not Found",
                "The patient information could not be found. Please try again or contact support.",
                404
        );
    }
}
