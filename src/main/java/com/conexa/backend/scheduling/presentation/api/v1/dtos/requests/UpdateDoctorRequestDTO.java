package com.conexa.backend.scheduling.presentation.api.v1.dtos.requests;

import com.conexa.backend.scheduling.presentation.api.v1.validators.MinAge;
import com.conexa.backend.scheduling.presentation.api.v1.validators.ValidTelephone;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdateDoctorRequestDTO(
        String specialty,

        @Past(message = "Birth date must be in the past")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @MinAge(value = 18, message = "Doctor must be at least 18 years old")
        LocalDate birthDate,

        @ValidTelephone
        String telephone
) {}
