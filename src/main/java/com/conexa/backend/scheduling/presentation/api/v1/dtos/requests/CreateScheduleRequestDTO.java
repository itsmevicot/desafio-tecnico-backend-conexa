package com.conexa.backend.scheduling.presentation.api.v1.dtos.requests;

import com.conexa.backend.scheduling.presentation.api.v1.deserializers.CustomLocalDateTimeDeserializer;
import com.conexa.backend.scheduling.presentation.api.v1.dtos.PatientDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateScheduleRequestDTO(

        @NotNull(message = "Date and time are mandatory")
        @Future(message = "The schedule date and time must be in the future")
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        LocalDateTime dateTime,

        @NotNull(message = "Patient information is mandatory")
        @Valid
        PatientDTO patient
) {}
