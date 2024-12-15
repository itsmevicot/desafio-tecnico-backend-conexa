package com.conexa.backend.scheduling.domain.exceptions.doctor;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class DoctorNotAvailableException extends ExceptionMessageBuilder {
    public DoctorNotAvailableException() {
        super(
                "Doctor Not Available",
                "The selected doctor is not available at the chosen time.",
                409
        );
    }
}

