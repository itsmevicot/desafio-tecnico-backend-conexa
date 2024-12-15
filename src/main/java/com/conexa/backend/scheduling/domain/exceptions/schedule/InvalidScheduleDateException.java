package com.conexa.backend.scheduling.domain.exceptions.schedule;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;


public class InvalidScheduleDateException extends ExceptionMessageBuilder {
    public InvalidScheduleDateException() {
        super(
                "Invalid Schedule Date",
                "Schedules can only be created for future dates.",
                400
        );
    }
}
