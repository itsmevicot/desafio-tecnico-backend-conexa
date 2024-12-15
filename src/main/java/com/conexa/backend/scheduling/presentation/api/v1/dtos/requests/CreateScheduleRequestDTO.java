package com.conexa.backend.scheduling.presentation.api.v1.dtos.requests;

import com.conexa.backend.scheduling.presentation.api.v1.dtos.PatientDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateScheduleRequestDTO(

        @NotNull(message = "Date and time are mandatory")
        @Future(message = "The schedule date and time must be in the future")
        LocalDateTime dateTime,

        @NotNull(message = "Patient information is mandatory")
        PatientDTO patient
) {}
