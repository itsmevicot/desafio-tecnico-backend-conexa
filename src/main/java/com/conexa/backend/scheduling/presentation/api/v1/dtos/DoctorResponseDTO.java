package com.conexa.backend.scheduling.presentation.api.v1.dtos;

import java.time.LocalDate;

public record DoctorResponseDTO(
        Long id,
        String email,
        String specialty,
        String cpf,
        LocalDate birthDate,
        String phone
) {}
