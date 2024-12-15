package com.conexa.backend.scheduling.presentation.api.v1.dtos.responses;

public record PatientResponseDTO(
        Long id,
        String name,
        String cpf,
        boolean active
) {}
