package com.conexa.backend.scheduling.domain.exceptions.schedule;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class ConflictingScheduleException extends ExceptionMessageBuilder {
    public ConflictingScheduleException(String message) {
        super(
                "Schedule Conflict",
                message,
                409
        );
    }
}
