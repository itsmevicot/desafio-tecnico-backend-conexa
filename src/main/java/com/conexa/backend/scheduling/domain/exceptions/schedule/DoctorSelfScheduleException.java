package com.conexa.backend.scheduling.domain.exceptions.schedule;

import com.conexa.backend.scheduling.domain.exceptions.ExceptionMessageBuilder;

public class DoctorSelfScheduleException extends ExceptionMessageBuilder {
    public DoctorSelfScheduleException() {
        super(
                "Doctor Self Schedule",
                "Doctors cannot schedule appointments with themselves.",
                400
        );
    }
}
